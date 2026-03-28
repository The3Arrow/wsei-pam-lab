package lab3

import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import pl.wsei.pam.lab01.R
import java.util.Stack

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val icons: List<Int> = listOf(
        R.drawable.baseline_rocket_24,
        R.drawable.outline_add_reaction_24,
        R.drawable.baseline_music_note_24,
        R.drawable.baseline_theater_comedy_24,
        R.drawable.baseline_anchor_24,
        R.drawable.baseline_airplane_ticket_24,
        R.drawable.baseline_directions_bike_24,
        R.drawable.baseline_alarm_24,
        R.drawable.ic_launcher_foreground,
        R.drawable.baseline_rocket_24,
        R.drawable.outline_add_reaction_24,
        R.drawable.baseline_music_note_24,
        R.drawable.baseline_theater_comedy_24,
        R.drawable.baseline_anchor_24,
        R.drawable.baseline_airplane_ticket_24,
        R.drawable.baseline_directions_bike_24,
        R.drawable.baseline_alarm_24,
        R.drawable.ic_launcher_foreground,
    )

    init {
        val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
            val numPairs = (cols * rows) / 2
            it.addAll(icons.subList(0, numPairs))
            it.addAll(icons.subList(0, numPairs))
            it.shuffle()
        }

        gridLayout.removeAllViews()
        gridLayout.columnCount = cols
        gridLayout.rowCount = rows

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val btn = ImageButton(gridLayout.context).also {
                    it.tag = "${row}x${col}"
                    val layoutParams = GridLayout.LayoutParams()
                    layoutParams.width = 0
                    layoutParams.height = 0
                    layoutParams.setGravity(Gravity.CENTER)
                    layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                    layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                    it.layoutParams = layoutParams
                    gridLayout.addView(it)
                }
                val icon = shuffledIcons.removeAt(0)
                addTile(btn, icon)
            }
        }
    }

    private val deckResource: Int = R.drawable.deck
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag] ?: return
        if (tile.revealed || matchedPair.size >= 2) return

        tile.revealed = true
        matchedPair.push(tile)
        val matchResult = logic.process {
            tile.tileResource
        }
        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))
        if (matchResult != GameStates.Matching) {
            matchedPair.clear()
        }
    }

    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }

    fun getState(): IntArray {
        val state = IntArray(rows * cols)
        var index = 0
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val tile = tiles["${row}x${col}"]
                if (tile != null && tile.revealed) {
                    state[index] = tile.tileResource
                } else {
                    state[index] = -1
                }
                index++
            }
        }
        return state
    }

    fun setState(state: IntArray) {
        val revealedIcons = state.filter { it != -1 }
        logic.matches = revealedIcons.size / 2
        
        val remainingPairsCount = (rows * cols - revealedIcons.size) / 2
        
        val availableIcons = icons.toMutableList()
        revealedIcons.distinct().forEach { icon ->
            availableIcons.remove(icon)
        }
        
        val newIcons: MutableList<Int> = mutableListOf<Int>().also {
            it.addAll(availableIcons.subList(0, remainingPairsCount))
            it.addAll(availableIcons.subList(0, remainingPairsCount))
            it.shuffle()
        }

        var stateIndex = 0
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val tile = tiles["${row}x${col}"]
                val resourceId = state[stateIndex]
                if (resourceId != -1) {
                    tile?.tileResource = resourceId
                    tile?.revealed = true
                } else {
                    val newIcon = if (newIcons.isNotEmpty()) newIcons.removeAt(0) else 0
                    tile?.tileResource = newIcon
                    tile?.revealed = false
                }
                stateIndex++
            }
        }
    }
}
