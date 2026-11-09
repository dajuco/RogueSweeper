package com.example.roguesweeper.presentation

import android.view.View

/**
 * Controlador d'esdeveniments d'entrada tàctil per a les caselles del tauler
 * segons el roadmap de presentació (Iker):
 * - Toc curt (Tap): descobrir casella
 * - Toc llarg (Long Press): col·locar o treure bandera
 */
class InputController(
    private val onCellClick: (row: Int, col: Int) -> Unit,
    private val onCellLongClick: (row: Int, col: Int) -> Unit
) {
    fun attachTo(view: View, row: Int, col: Int) {
        view.setOnClickListener {
            onCellClick(row, col)
        }
        view.setOnLongClickListener {
            onCellLongClick(row, col)
            true
        }
    }
}
