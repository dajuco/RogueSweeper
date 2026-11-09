package com.example.roguesweeper.presentation.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roguesweeper.R
import com.example.roguesweeper.data.file.MockProfileRepository
import com.example.roguesweeper.databinding.ActivityMainBinding
import com.example.roguesweeper.domain.model.PlayerProfile
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedSlotId: String = "slot_1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSlotSelector()
        setupActionButtons()
        refreshProfileUI()
    }

    override fun onResume() {
        super.onResume()
        // Actualitza les dades del perfil en tornar de la partida o la botiga
        refreshProfileUI()
    }

    private fun setupSlotSelector() {
        binding.toggleGroupSlots.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedSlotId = when (checkedId) {
                    R.id.btnSlot2 -> "slot_2"
                    else -> "slot_1"
                }
                refreshProfileUI()
            }
        }
    }

    private fun setupActionButtons() {
        // Botó Jugar: inicia la pantalla de joc de RogueSweeper
        binding.btnPlayGame.setOnClickListener {
            val intent = GameActivity.newIntent(this, selectedSlotId)
            startActivity(intent)
        }

        // Botó Botiga de Millores
        binding.btnOpenShop.setOnClickListener {
            val intent = ShopActivity.newIntent(this, selectedSlotId)
            startActivity(intent)
        }

        // Botó Reiniciar Ranura
        binding.btnResetSlot.setOnClickListener {
            showResetConfirmationDialog()
        }
    }

    private fun refreshProfileUI() {
        val profile: PlayerProfile = MockProfileRepository.loadProfile(selectedSlotId)
        val slotDisplayName = if (selectedSlotId == "slot_1") {
            getString(R.string.slot_1)
        } else {
            getString(R.string.slot_2)
        }

        binding.tvProfileTitle.text = "Estat del Perfil ($slotDisplayName)"
        binding.tvProfilePoints.text = "${profile.points} pts"
        binding.tvProfileShields.text = "${profile.shields} escut(s)"

        val activeUpgradesCount = profile.upgrades.count { it.level > 0 }
        binding.tvProfileUpgrades.text = "$activeUpgradesCount / ${profile.upgrades.size}"
    }

    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.reset_confirm_title))
            .setMessage(getString(R.string.reset_confirm_message))
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                MockProfileRepository.resetProfile(selectedSlotId)
                refreshProfileUI()
                Snackbar.make(
                    binding.root,
                    "Ranura reiniciada correctament.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}
