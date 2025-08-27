package com.example.board.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.*;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(unique = true)
    private String name;


    // Ordenação pelo campo "position" da coluna
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<BoardColumn> columns = new ArrayList<>();
}