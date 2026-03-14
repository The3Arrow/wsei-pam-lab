package lab02

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab02)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun onBoardSizeClick(view: View) {
        // Rzutowanie tagu na String
        val tagString = view.tag as String

        val dimensions = tagString.split(" ")

        if (dimensions.size == 2) {
            val columns = dimensions[0].toInt()
            val rows = dimensions[1].toInt()

            Toast.makeText(this, "Wybrano planszę: $columns kolumn x $rows wierszy", Toast.LENGTH_SHORT).show()
        }
    }
}