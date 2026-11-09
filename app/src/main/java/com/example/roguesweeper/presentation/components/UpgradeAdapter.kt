package com.example.roguesweeper.presentation.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roguesweeper.R
import com.example.roguesweeper.databinding.ItemUpgradeBinding
import com.example.roguesweeper.domain.model.Upgrade

class UpgradeAdapter(
    private var upgrades: List<Upgrade>,
    private val onBuyClicked: (Upgrade) -> Unit
) : RecyclerView.Adapter<UpgradeAdapter.UpgradeViewHolder>() {

    fun updateList(newUpgrades: List<Upgrade>) {
        upgrades = newUpgrades
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpgradeViewHolder {
        val binding = ItemUpgradeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpgradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpgradeViewHolder, position: Int) {
        holder.bind(upgrades[position])
    }

    override fun getItemCount(): Int = upgrades.size

    inner class UpgradeViewHolder(private val binding: ItemUpgradeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(upgrade: Upgrade) {
            val context = binding.root.context

            // Icon mapping
            binding.tvUpgradeIcon.text = when (upgrade.id) {
                "shield_capacity" -> "🛡️"
                "initial_reveal" -> "🔍"
                "mine_value" -> "⭐"
                "board_size" -> "🗺️"
                else -> "⚡"
            }

            binding.tvUpgradeName.text = upgrade.name
            binding.tvUpgradeDesc.text = upgrade.description
            binding.tvUpgradeLevel.text = "Nivell ${upgrade.level} / ${upgrade.maxLevel}"

            if (upgrade.isMaxLevel) {
                binding.tvUpgradeCost.text = "Completat"
                binding.tvUpgradeCost.setTextColor(
                    ContextCompat.getColor(context, R.color.text_secondary)
                )
                binding.btnBuyUpgrade.isEnabled = false
                binding.btnBuyUpgrade.text = context.getString(R.string.max_level_reached)
                binding.btnBuyUpgrade.alpha = 0.5f
                binding.tvUpgradeLevel.setTextColor(
                    ContextCompat.getColor(context, R.color.success_emerald)
                )
            } else {
                binding.tvUpgradeCost.text = "🪙 ${upgrade.currentCost} punts"
                binding.tvUpgradeCost.setTextColor(
                    ContextCompat.getColor(context, R.color.accent_gold)
                )
                binding.btnBuyUpgrade.isEnabled = true
                binding.btnBuyUpgrade.text = context.getString(R.string.btn_upgrade)
                binding.btnBuyUpgrade.alpha = 1.0f
                binding.tvUpgradeLevel.setTextColor(
                    ContextCompat.getColor(context, R.color.shield_cyan)
                )
            }

            binding.btnBuyUpgrade.setOnClickListener {
                onBuyClicked(upgrade)
            }
        }
    }
}
