package com.example.aircraftwar2024.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aircraftwar2024.R

class OfflineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val musicState = intent.getBooleanExtra("MUSIC_STATE", false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_offline)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button1: Button = findViewById(R.id.StandAloneDifficultySelectButton1)
        button1.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("gameType", 1)
            intent.putExtra("MUSIC_STATE", musicState)
            startActivity(intent)
        }
        val button2: Button = findViewById(R.id.StandAloneDifficultySelectButton2)
        button2.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("gameType", 2)
            intent.putExtra("MUSIC_STATE", musicState)
            startActivity(intent)
        }
        val button3: Button = findViewById(R.id.StandAloneDifficultySelectButton3)
        button3.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("gameType", 3)
            intent.putExtra("MUSIC_STATE", musicState)
            startActivity(intent)
        }

    }
}