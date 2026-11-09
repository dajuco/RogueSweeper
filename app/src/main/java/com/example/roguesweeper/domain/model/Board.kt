package com.example.roguesweeper.domain.model

/**
 * Representa el tauler complet del joc amb les seves dimensions i contingut de cel·les.
 */
data class Board(
    val rows: Int = 8,
    val cols: Int = 8,
    val totalMines: Int = 10,
    val grid: List<List<Cell>> = emptyList()
)
