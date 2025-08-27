package com.example.board.domain;


public enum ColumnType {
    INITIAL, // primeira coluna
    PENDING, // pendentes (0..N)
    FINAL, // penúltima coluna
    CANCEL // última coluna
}