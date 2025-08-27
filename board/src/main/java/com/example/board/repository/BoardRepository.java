package com.example.board.repository;


import com.example.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByNameIgnoreCase(String name);
}