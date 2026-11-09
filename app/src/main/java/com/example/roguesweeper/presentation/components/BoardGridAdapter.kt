package com.example.roguesweeper.presentation.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roguesweeper.R
import com.example.roguesweeper.databinding.ItemBoardCellBinding
import com.example.roguesweeper.domain.model.Cell
import com.example.roguesweeper.presentation.InputController

class BoardGridAdapter(
    private var cells: List<Cell>,
    private val onCellClick: (row: Int, col: Int) -> Unit,
    private val onCellLongClick: (row: Int, col: Int) -> Unit
) : RecyclerView.Adapter<BoardGridAdapter.CellViewHolder>() {

    private val inputController = InputController(
        onCellClick = { r, c -> onCellClick(r, c) },
        onCellLongClick = { r, c -> onCellLongClick(r, c) }
    )

    fun updateCells(newCells: List<Cell>) {
        cells = newCells
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = ItemBoardCellBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(cells[position])
    }

    override fun getItemCount(): Int = cells.size

    inner class CellViewHolder(private val binding: ItemBoardCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cell: Cell) {
            val context = binding.root.context
            val tv = binding.tvCell

            // Input handling using InputController
            inputController.attachTo(tv, cell.row, cell.col)

            if (cell.isRevealed) {
                if (cell.hasMine) {
                    tv.setBackgroundResource(R.drawable.bg_cell_mine)
                    tv.text = "💣"
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    tv.setBackgroundResource(R.drawable.bg_cell_revealed)
                    if (cell.adjacentMines > 0) {
                        tv.text = cell.adjacentMines.toString()
                        val colorRes = when (cell.adjacentMines) {
                            1 -> R.color.num_1
                            2 -> R.color.num_2
                            3 -> R.color.num_3
                            4 -> R.color.num_4
                            5 -> R.color.num_5
                            6 -> R.color.num_6
                            7 -> R.color.num_7
                            else -> R.color.num_8
                        }
                        tv.setTextColor(ContextCompat.getColor(context, colorRes))
                    } else {
                        tv.text = ""
                    }
                }
            } else {
                tv.setBackgroundResource(R.drawable.bg_cell_covered)
                if (cell.isFlagged) {
                    tv.text = "🚩"
                    tv.setTextColor(ContextCompat.getColor(context, R.color.accent_gold))
                } else {
                    tv.text = ""
                }
            }
        }
    }
}
