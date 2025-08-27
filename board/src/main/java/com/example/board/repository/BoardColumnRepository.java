package com.example.board.repository;


import com.example.board.domain.Board;
import com.example.board.domain.BoardColumn;
import com.example.board.domain.ColumnType;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.*;


public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoardOrderByPositionAsc(Board board);
    Optional<BoardColumn> findByBoardAndType(Board board, ColumnType type);
}