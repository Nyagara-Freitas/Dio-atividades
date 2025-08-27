package com.example.board.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.time.Instant;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Card {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private BoardColumn column;


    @NotBlank
    private String title;


    private String description;


    private Instant createdAt;


    private boolean blocked;


    // Guarda a última razão informada de bloqueio/desbloqueio
    private String lastReason;


    @PrePersist
    void onCreate(){
        if (createdAt == null) createdAt = Instant.now();
    }
}