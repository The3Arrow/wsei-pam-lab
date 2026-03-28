package lab3

import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import java.util.Timer
import kotlin.concurrent.schedule

class Lab003Activity : AppCompatActivity() {
    private lateinit var mBoard: GridLayout
    private lateinit var mBoardModel: MemoryBoardView
    private var columns: Int = 4
    private var rows: Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab003)

        mBoard = findViewById(R.id.main)

        val size = intent.getIntArrayExtra("size") ?: intArrayOf(4, 4)
        columns = size[0]
        rows = size[1]

        if (savedInstanceState != null) {
            val state = savedInstanceState.getIntArray("gameState")
            mBoardModel = MemoryBoardView(mBoard, columns, rows)
            if (state != null) {
                mBoardModel.setState(state)
            }
        } else {
            mBoardModel = MemoryBoardView(mBoard, columns, rows)
        }

        setupGameChangeListener()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupGameChangeListener() {
        mBoardModel.setOnGameChangeListener { e ->
            run {
                when (e.state) {
                    GameStates.Matching -> {
                        e.tiles.forEach { it.revealed = true }
                    }
                    GameStates.Match -> {
                        e.tiles.forEach { it.revealed = true }
                    }
                    GameStates.NoMatch -> {
                        e.tiles.forEach { it.revealed = true }
                        Timer().schedule(2000) {
                            runOnUiThread {
                                e.tiles.forEach { it.revealed = false }
                            }
                        }
                    }
                    GameStates.Finished -> {
                        e.tiles.forEach { it.revealed = true }
                        Toast.makeText(this, "Koniec gry", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("gameState", mBoardModel.getState())
    }
}
