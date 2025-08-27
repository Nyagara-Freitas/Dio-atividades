package com.example.board.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.*;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BoardColumn {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;


    @NotBlank
    private String name;


    @Enumerated(EnumType.STRING)
    private ColumnType type;


    // posição/ordem visual no board (0..N)
    private int position;


    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Card> cards = new ArrayList<>();
}