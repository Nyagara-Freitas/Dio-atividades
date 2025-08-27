package com.example.board.repository;


import com.example.board.domain.Card;
import com.example.board.domain.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByColumn(BoardColumn column);
}