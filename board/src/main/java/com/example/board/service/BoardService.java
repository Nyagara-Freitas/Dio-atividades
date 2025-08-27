package com.example.board.service;

import com.example.board.domain.*;
import com.example.board.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BoardService {
    private final BoardRepository boardRepo;
    private final BoardColumnRepository columnRepo;
    private final CardRepository cardRepo;

    public BoardService(BoardRepository boardRepo, BoardColumnRepository columnRepo, CardRepository cardRepo) {
        this.boardRepo = boardRepo;
        this.columnRepo = columnRepo;
        this.cardRepo = cardRepo;
    }

    // ===== Boards =====
    @Transactional
    public Board createBoard(String name, List<String> pendingNames, String initialName, String finalName, String cancelName) {
        if (boardRepo.findByNameIgnoreCase(name).isPresent()) {
            throw new IllegalArgumentException("Já existe um board com esse nome.");
        }
        if (pendingNames == null) pendingNames = List.of();

        List<BoardColumn> cols = new ArrayList<>();
        Board board = Board.builder().name(name).build();
        board = boardRepo.save(board);

        int position = 0;
        cols.add(columnRepo.save(BoardColumn.builder()
                .board(board).name(initialName).type(ColumnType.INITIAL).position(position++)
                .build()));

        for (String p : pendingNames) {
            cols.add(columnRepo.save(BoardColumn.builder()
                    .board(board).name(p).type(ColumnType.PENDING).position(position++)
                    .build()));
        }

        cols.add(columnRepo.save(BoardColumn.builder()
                .board(board).name(finalName).type(ColumnType.FINAL).position(position++)
                .build()));

        cols.add(columnRepo.save(BoardColumn.builder()
                .board(board).name(cancelName).type(ColumnType.CANCEL).position(position)
                .build()));

        board.setColumns(cols);
        validateBoardColumns(board);
        return boardRepo.save(board);
    }

    public List<Board> listBoards() {
        return boardRepo.findAll();
    }

    @Transactional
    public void deleteAllBoards() {
        boardRepo.deleteAll();
    }

    public Optional<Board> getBoardById(Long id) {
        return boardRepo.findById(id);
    }

    // ===== Cards =====
    @Transactional
    public Card createCard(Board board, String title, String description) {
        BoardColumn initial = columnRepo.findByBoardAndType(board, ColumnType.INITIAL)
                .orElseThrow(() -> new IllegalStateException("Board sem coluna INITIAL."));
        Card card = Card.builder()
                .column(initial)
                .title(title)
                .description(description)
                .blocked(false)
                .build();
        return cardRepo.save(card);
    }

    @Transactional
    public Card blockCard(Long cardId, String reason) {
        Card c = cardRepo.findById(cardId).orElseThrow(() -> new NoSuchElementException("Card não encontrado"));
        c.setBlocked(true);
        c.setLastReason("BLOQUEADO: " + (reason == null ? "(sem motivo)" : reason));
        return cardRepo.save(c);
    }

    @Transactional
    public Card unblockCard(Long cardId, String reason) {
        Card c = cardRepo.findById(cardId).orElseThrow(() -> new NoSuchElementException("Card não encontrado"));
        c.setBlocked(false);
        c.setLastReason("DESBLOQUEADO: " + (reason == null ? "(sem motivo)" : reason));
        return cardRepo.save(c);
    }

    @Transactional
    public Card moveToNext(Long cardId) {
        Card c = cardRepo.findById(cardId).orElseThrow(() -> new NoSuchElementException("Card não encontrado"));
        if (c.isBlocked()) throw new IllegalStateException("Card bloqueado não pode ser movido.");

        BoardColumn current = c.getColumn();
        Board board = current.getBoard();
        List<BoardColumn> cols = columnRepo.findByBoardOrderByPositionAsc(board);

        if (current.getType() == ColumnType.CANCEL)
            throw new IllegalStateException("Card em coluna CANCEL não avança.");
        if (current.getType() == ColumnType.FINAL)
            throw new IllegalStateException("Card já está na coluna FINAL.");

        int nextPos = current.getPosition() + 1;
        BoardColumn next = cols.stream().filter(col -> col.getPosition() == nextPos)
                .findFirst().orElseThrow(() -> new IllegalStateException("Próxima coluna não encontrada."));

        c.setColumn(next);
        return cardRepo.save(c);
    }

    @Transactional
    public Card cancelCard(Long cardId) {
        Card c = cardRepo.findById(cardId).orElseThrow(() -> new NoSuchElementException("Card não encontrado"));
        if (c.isBlocked()) throw new IllegalStateException("Card bloqueado não pode ser movido.");

        BoardColumn current = c.getColumn();
        if (current.getType() == ColumnType.FINAL)
            throw new IllegalStateException("Card na coluna FINAL não pode ser cancelado.");

        BoardColumn cancel = columnRepo.findByBoardAndType(current.getBoard(), ColumnType.CANCEL)
                .orElseThrow(() -> new IllegalStateException("Coluna CANCEL não encontrada"));
        c.setColumn(cancel);
        return cardRepo.save(c);
    }

    // ===== Validação =====
    private void validateBoardColumns(Board board) {
        List<BoardColumn> cols = columnRepo.findByBoardOrderByPositionAsc(board);
        if (cols.size() < 3) throw new IllegalStateException("O board deve ter pelo menos 3 colunas.");

        Optional<BoardColumn> initial = cols.stream().filter(c -> c.getType() == ColumnType.INITIAL).findFirst();
        Optional<BoardColumn> finalCol = cols.stream().filter(c -> c.getType() == ColumnType.FINAL).findFirst();
        Optional<BoardColumn> cancel = cols.stream().filter(c -> c.getType() == ColumnType.CANCEL).findFirst();

        if (initial.isEmpty() || finalCol.isEmpty() || cancel.isEmpty())
            throw new IllegalStateException("Board deve ter exatamente 1 coluna INITIAL, 1 FINAL e 1 CANCEL.");

        int size = cols.size();
        if (initial.get().getPosition() != 0)
            throw new IllegalStateException("Coluna INITIAL deve ser a posição 0.");
        if (finalCol.get().getPosition() != size - 2)
            throw new IllegalStateException("Coluna FINAL deve ser a penúltima.");
        if (cancel.get().getPosition() != size - 1)
            throw new IllegalStateException("Coluna CANCEL deve ser a última.");

        // verifica posições sequenciais
        for (int i = 0; i < size; i++) {
            if (cols.get(i).getPosition() != i)
                throw new IllegalStateException("Posições das colunas devem ser sequenciais (0..N).");
        }
    }
}
