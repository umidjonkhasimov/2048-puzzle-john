package uz.gita.my2048puzzleumidjon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.my2048puzzle.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<AppCompatButton>(R.id.btnStartGame).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btnQuitGame).setOnClickListener {
            finish()
        }
    }
}