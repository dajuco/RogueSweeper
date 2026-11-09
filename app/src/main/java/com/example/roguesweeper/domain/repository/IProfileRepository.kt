package com.example.roguesweeper.domain.repository

import com.example.roguesweeper.domain.model.PlayerProfile

/**
 * Contracte per a la persistència de perfils (complint amb les especificacions del roadmap).
 * David implementarà JsonProfileRepository (RA1) i SqlProfileRepository (RA2).
 */
interface IProfileRepository {
    fun loadProfile(slotId: String): PlayerProfile
    fun saveProfile(profile: PlayerProfile)
    fun resetProfile(slotId: String): PlayerProfile
    fun exportToCsv(slotId: String): String
}
