package com.example.roguesweeper.presentation.components

import android.view.LayoutInflater
import android.view.View
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
            val container = binding.cellContainer
            val tv = binding.tvCell
            val iv = binding.ivCellIcon

            inputController.attachTo(container, cell.row, cell.col)

            if (cell.isRevealed) {
                if (cell.hasMine) {
                    container.setBackgroundResource(R.drawable.bg_cell_mine)
                    tv.visibility = View.GONE
                    iv.visibility = View.VISIBLE
                    iv.setImageResource(R.drawable.ic_mine)
                } else {
                    container.setBackgroundResource(R.drawable.bg_cell_revealed)
                    iv.visibility = View.GONE
                    if (cell.adjacentMines > 0) {
                        tv.visibility = View.VISIBLE
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
                        tv.visibility = View.GONE
                        tv.text = ""
                    }
                }
            } else {
                if (cell.isFlagged) {
                    container.setBackgroundResource(R.drawable.bg_cell_flagged)
                    tv.visibility = View.GONE
                    iv.visibility = View.VISIBLE
                    iv.setImageResource(R.drawable.ic_flag)
                } else {
                    container.setBackgroundResource(R.drawable.bg_cell_covered)
                    tv.visibility = View.GONE
                    iv.visibility = View.GONE
                    tv.text = ""
                }
            }
        }
    }
}
