package com.example.roguesweeper.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roguesweeper.R
import com.example.roguesweeper.data.file.MockProfileRepository
import com.example.roguesweeper.databinding.ActivityShopBinding
import com.example.roguesweeper.domain.model.PlayerProfile
import com.example.roguesweeper.domain.model.Upgrade
import com.example.roguesweeper.presentation.components.UpgradeAdapter
import com.google.android.material.snackbar.Snackbar

class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding
    private lateinit var adapter: UpgradeAdapter
    private var slotId: String = "slot_1"
    private lateinit var currentProfile: PlayerProfile

    companion object {
        private const val EXTRA_SLOT_ID = "extra_slot_id"

        fun newIntent(context: Context, slotId: String): Intent {
            return Intent(context, ShopActivity::class.java).apply {
                putExtra(EXTRA_SLOT_ID, slotId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        slotId = intent.getStringExtra(EXTRA_SLOT_ID) ?: "slot_1"

        setupViews()
        loadProfileData()
    }

    private fun setupViews() {
        val slotLabel = if (slotId == "slot_1") getString(R.string.slot_1) else getString(R.string.slot_2)
        binding.tvShopSlotIndicator.text = slotLabel

        binding.btnBackToMenu.setOnClickListener {
            finish()
        }

        adapter = UpgradeAdapter(emptyList()) { upgrade ->
            processUpgradePurchase(upgrade)
        }

        binding.rvUpgrades.layoutManager = LinearLayoutManager(this)
        binding.rvUpgrades.adapter = adapter
    }

    private fun loadProfileData() {
        currentProfile = MockProfileRepository.loadProfile(slotId)
        binding.tvShopBalance.text = "${currentProfile.points} punts"
        adapter.updateList(currentProfile.upgrades)
    }

    private fun processUpgradePurchase(upgrade: Upgrade) {
        if (upgrade.isMaxLevel) return

        val cost = upgrade.currentCost
        if (currentProfile.points < cost) {
            Snackbar.make(
                binding.root,
                getString(R.string.insufficient_points),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        // Simula la transacció de compra (que David connectarà amb UpgradeService / TransactionHelper)
        val updatedPoints = currentProfile.points - cost
        val updatedUpgrades = currentProfile.upgrades.map { item ->
            if (item.id == upgrade.id) {
                item.copy(level = item.level + 1)
            } else {
                item
            }
        }

        // Si es millora la capacitat d'escuts, actualitzem els escuts inicials del perfil
        val newShields = if (upgrade.id == "shield_capacity") {
            currentProfile.shields + 1
        } else {
            currentProfile.shields
        }

        val updatedProfile = currentProfile.copy(
            points = updatedPoints,
            shields = newShields,
            upgrades = updatedUpgrades
        )

        MockProfileRepository.saveProfile(updatedProfile)
        currentProfile = updatedProfile

        binding.tvShopBalance.text = "${currentProfile.points} punts"
        adapter.updateList(currentProfile.upgrades)

        Snackbar.make(
            binding.root,
            "${getString(R.string.upgrade_success)} (${upgrade.name} Lv.${upgrade.level + 1})",
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
