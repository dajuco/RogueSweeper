package com.example.roguesweeper.domain.model

/**
 * Representa una millora permanent que el jugador pot adquirir a la botiga.
 */
data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val level: Int,
    val maxLevel: Int,
    val baseCost: Int
) {
    val isMaxLevel: Boolean
        get() = level >= maxLevel

    val currentCost: Int
        get() = if (isMaxLevel) 0 else (baseCost * (level + 1))
}
