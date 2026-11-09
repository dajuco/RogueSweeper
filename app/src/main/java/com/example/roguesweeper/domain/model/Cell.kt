package com.example.roguesweeper.domain.model

/**
 * Representa una casella individual del tauler de RogueSweeper.
 */
data class Cell(
    val row: Int,
    val col: Int,
    val hasMine: Boolean = false,
    val isRevealed: Boolean = false,
    val isFlagged: Boolean = false,
    val adjacentMines: Int = 0
)
