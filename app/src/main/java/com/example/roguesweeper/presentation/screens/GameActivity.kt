package com.example.roguesweeper.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.roguesweeper.R
import com.example.roguesweeper.data.file.MockProfileRepository
import com.example.roguesweeper.databinding.ActivityGameBinding
import com.example.roguesweeper.domain.model.Cell
import com.example.roguesweeper.domain.model.PlayerProfile
import com.example.roguesweeper.presentation.components.BoardGridAdapter
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var adapter: BoardGridAdapter

    private var slotId: String = "slot_1"
    private lateinit var profile: PlayerProfile

    // Game state
    private val rows = 8
    private val cols = 8
    private val totalMines = 10
    private var board: Array<Array<Cell>> = Array(rows) { r ->
        Array(cols) { c -> Cell(row = r, col = c) }
    }
    private var isFirstClick = true
    private var currentShields = 1
    private var sessionPoints = 0
    private var isGameOver = false
    private var isFlagMode = false

    companion object {
        private const val EXTRA_SLOT_ID = "extra_slot_id"

        fun newIntent(context: Context, slotId: String): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_SLOT_ID, slotId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        slotId = intent.getStringExtra(EXTRA_SLOT_ID) ?: "slot_1"
        profile = MockProfileRepository.loadProfile(slotId)
        currentShields = profile.shields

        setupViews()
        initializeBoard()
        applyUpgrades()
        updateHud()
    }

    private fun setupViews() {
        val slotLabel = if (slotId == "slot_1") getString(R.string.slot_1) else getString(R.string.slot_2)
        binding.tvGameSlotLabel.text = "Partida: $slotLabel"

        binding.btnSurrender.setOnClickListener {
            showSurrenderDialog()
        }

        binding.toggleModeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                isFlagMode = (checkedId == R.id.btnModeFlag)
            }
        }

        adapter = BoardGridAdapter(
            cells = getFlattenedBoard(),
            onCellClick = { r, c -> handleCellClick(r, c) },
            onCellLongClick = { r, c -> toggleFlag(r, c) }
        )

        binding.rvBoardGrid.layoutManager = GridLayoutManager(this, cols)
        binding.rvBoardGrid.adapter = adapter
    }

    private fun initializeBoard() {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                board[r][c] = Cell(row = r, col = c)
            }
        }
    }

    private fun applyUpgrades() {
        // Revelació inicial si s'ha comprat la millora
        val revealUpgrade = profile.upgrades.find { it.id == "initial_reveal" }
        if (revealUpgrade != null && revealUpgrade.level > 0) {
            // Revela el centre per donar un inici segur
            generateMinesExcept(rows / 2, cols / 2)
            revealCell(rows / 2, cols / 2)
            isFirstClick = false
        }
    }

    private fun generateMinesExcept(safeRow: Int, safeCol: Int) {
        var placed = 0
        while (placed < totalMines) {
            val r = Random.nextInt(rows)
            val c = Random.nextInt(cols)
            if ((r == safeRow && c == safeCol) || board[r][c].hasMine) continue

            board[r][c] = board[r][c].copy(hasMine = true)
            placed++
        }

        // Càlcul de mines adjacents
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (!board[r][c].hasMine) {
                    val count = countAdjacentMines(r, c)
                    board[r][c] = board[r][c].copy(adjacentMines = count)
                }
            }
        }
    }

    private fun countAdjacentMines(row: Int, col: Int): Int {
        var count = 0
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = row + dr
                val nc = col + dc
                if (nr in 0 until rows && nc in 0 until cols && board[nr][nc].hasMine) {
                    count++
                }
            }
        }
        return count
    }

    private fun handleCellClick(row: Int, col: Int) {
        if (isGameOver) return

        if (isFlagMode) {
            toggleFlag(row, col)
            return
        }

        val cell = board[row][col]
        if (cell.isRevealed || cell.isFlagged) return

        if (isFirstClick) {
            generateMinesExcept(row, col)
            isFirstClick = false
        }

        if (cell.hasMine) {
            // Trepitjada de mina amb sistema roguelite d'escuts
            if (currentShields > 0) {
                currentShields--
                board[row][col] = cell.copy(isRevealed = true)
                updateHud()
                refreshBoard()
                Snackbar.make(binding.root, getString(R.string.shield_saved_toast), Snackbar.LENGTH_SHORT).show()
                checkVictoryCondition()
            } else {
                // Derrota
                triggerGameOver(false)
            }
        } else {
            revealCell(row, col)
            val mineValueUpgrade = profile.upgrades.find { it.id == "mine_value" }?.level ?: 1
            sessionPoints += (10 * mineValueUpgrade)
            updateHud()
            refreshBoard()
            checkVictoryCondition()
        }
    }

    private fun revealCell(row: Int, col: Int) {
        if (row !in 0 until rows || col !in 0 until cols) return
        val cell = board[row][col]
        if (cell.isRevealed || cell.isFlagged || cell.hasMine) return

        board[row][col] = cell.copy(isRevealed = true)

        // Cascada flood fill si és buida (0 mines adjacents)
        if (cell.adjacentMines == 0) {
            for (dr in -1..1) {
                for (dc in -1..1) {
                    if (dr != 0 || dc != 0) {
                        revealCell(row + dr, col + dc)
                    }
                }
            }
        }
    }

    private fun toggleFlag(row: Int, col: Int) {
        if (isGameOver) return
        val cell = board[row][col]
        if (cell.isRevealed) return

        board[row][col] = cell.copy(isFlagged = !cell.isFlagged)
        refreshBoard()
        updateHud()
    }

    private fun checkVictoryCondition() {
        var nonMineCovered = 0
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (!board[r][c].hasMine && !board[r][c].isRevealed) {
                    nonMineCovered++
                }
            }
        }

        if (nonMineCovered == 0) {
            triggerGameOver(true)
        }
    }

    private fun triggerGameOver(won: Boolean) {
        isGameOver = true
        // Revela totes les mines
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (board[r][c].hasMine) {
                    board[r][c] = board[r][c].copy(isRevealed = true)
                }
            }
        }
        refreshBoard()

        val victoryBonus = if (won) 100 else 0
        val totalEarned = sessionPoints + victoryBonus

        // Guarda els punts acumulats a la ranura permanent
        val updatedProfile = profile.copy(points = profile.points + totalEarned)
        MockProfileRepository.saveProfile(updatedProfile)

        val title = if (won) getString(R.string.victory_title) else getString(R.string.defeat_title)
        val msg = if (won) {
            "${getString(R.string.victory_message)}\n\n🪙 Punts obtinguts: $sessionPoints\n⭐ Bonus victòria: +$victoryBonus\n💰 Punts totals guanyats: $totalEarned"
        } else {
            "${getString(R.string.defeat_message)}\n\n🪙 Punts conservats: $sessionPoints\nS'han transferit al teu saldo permanent!"
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.claim_and_exit)) { _, _ ->
                finish()
            }
            .show()
    }

    private fun showSurrenderDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.surrender_confirm_title))
            .setMessage("${getString(R.string.surrender_confirm_msg)}\n\n🪙 Punts acumulats ara mateix: $sessionPoints")
            .setPositiveButton(getString(R.string.btn_surrender)) { _, _ ->
                if (sessionPoints > 0) {
                    val updatedProfile = profile.copy(points = profile.points + sessionPoints)
                    MockProfileRepository.saveProfile(updatedProfile)
                }
                finish()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun updateHud() {
        var flaggedCount = 0
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (board[r][c].isFlagged) flaggedCount++
            }
        }
        val remainingMines = (totalMines - flaggedCount).coerceAtLeast(0)
        binding.tvHudMines.text = remainingMines.toString()
        binding.tvHudShields.text = currentShields.toString()
        binding.tvHudPoints.text = sessionPoints.toString()
    }

    private fun getFlattenedBoard(): List<Cell> {
        val list = ArrayList<Cell>(rows * cols)
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                list.add(board[r][c])
            }
        }
        return list
    }

    private fun refreshBoard() {
        adapter.updateCells(getFlattenedBoard())
    }
}
