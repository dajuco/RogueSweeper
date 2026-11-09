package com.example.roguesweeper.data.file

import com.example.roguesweeper.domain.model.PlayerProfile
import com.example.roguesweeper.domain.repository.IProfileRepository

/**
 * Repositori temporal simulat (Mock) perquè la UI funcioni abans que David
 * connecti el JsonProfileRepository definit a la Fase 2.
 */
object MockProfileRepository : IProfileRepository {
    private val profiles = mutableMapOf<String, PlayerProfile>(
        "slot_1" to PlayerProfile.createDefault("slot_1"),
        "slot_2" to PlayerProfile.createDefault("slot_2")
    )

    override fun loadProfile(slotId: String): PlayerProfile {
        return profiles.getOrPut(slotId) { PlayerProfile.createDefault(slotId) }
    }

    override fun saveProfile(profile: PlayerProfile) {
        profiles[profile.slotId] = profile
    }

    override fun resetProfile(slotId: String): PlayerProfile {
        val defaultProfile = PlayerProfile.createDefault(slotId)
        profiles[slotId] = defaultProfile
        return defaultProfile
    }

    override fun exportToCsv(slotId: String): String {
        val profile = loadProfile(slotId)
        return "slotId,points,shields\n${profile.slotId},${profile.points},${profile.shields}"
    }
}
