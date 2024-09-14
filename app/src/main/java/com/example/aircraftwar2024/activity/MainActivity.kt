package com.example.aircraftwar2024.activity

import GameSocketClient
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aircraftwar2024.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button1: Button = findViewById(R.id.StandAloneGameButton)
        button1.setOnClickListener {
            val intent = Intent(this, OfflineActivity::class.java)
            val musicEnabled = findViewById<RadioButton>(R.id.Music_on).isChecked
            intent.putExtra("MUSIC_STATE", musicEnabled)
            startActivity(intent)
        }
        val button2: Button = findViewById(R.id.StandServerGameButton)
        button2.setOnClickListener {
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            val musicEnabled = findViewById<RadioButton>(R.id.Music_on).isChecked
            intent.putExtra("MUSIC_STATE", musicEnabled)
            intent.putExtra("ONLINE_STATE", true)
            startActivity(intent)
        }
    }
}