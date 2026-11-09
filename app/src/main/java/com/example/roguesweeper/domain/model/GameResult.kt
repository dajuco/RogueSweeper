package com.example.roguesweeper.domain.model

/**
 * Resultat d'una partida de RogueSweeper.
 */
data class GameResult(
    val won: Boolean,
    val pointsEarned: Int,
    val victoryBonus: Int,
    val totalPoints: Int
)
