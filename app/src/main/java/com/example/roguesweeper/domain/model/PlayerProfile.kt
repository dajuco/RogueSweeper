package com.example.roguesweeper.domain.model

/**
 * Representa el perfil guardat d'un jugador (saldo, escuts i millores adquirides).
 */
data class PlayerProfile(
    val slotId: String,
    val points: Int = 0,
    val shields: Int = 1,
    val upgrades: List<Upgrade> = defaultUpgrades()
) {
    companion object {
        fun defaultUpgrades(): List<Upgrade> = listOf(
            Upgrade(
                id = "shield_capacity",
                name = "Capacitat d'Escuts",
                description = "Permet sobreviure a detonacions accidentals per partida.",
                level = 1,
                maxLevel = 3,
                baseCost = 100
            ),
            Upgrade(
                id = "initial_reveal",
                name = "Revelació Inicial",
                description = "Comença cada partida amb una petita àrea segura revelada.",
                level = 0,
                maxLevel = 3,
                baseCost = 150
            ),
            Upgrade(
                id = "mine_value",
                name = "Valor de Mina",
                description = "Augmenta els punts obtinguts per cada mina detectada.",
                level = 1,
                maxLevel = 5,
                baseCost = 75
            ),
            Upgrade(
                id = "board_size",
                name = "Mida del Tauler",
                description = "Desbloqueja taulers més grans amb majors recompenses.",
                level = 1,
                maxLevel = 3,
                baseCost = 250
            )
        )

        fun createDefault(slotId: String): PlayerProfile = PlayerProfile(
            slotId = slotId,
            points = 250, // Punts inicials per poder testejar la botiga
            shields = 1,
            upgrades = defaultUpgrades()
        )
    }
}
