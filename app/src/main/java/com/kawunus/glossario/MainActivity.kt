package com.kawunus.glossario

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kawunus.glossario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        init()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (prefs.getString("status", "noreg") == "noreg") {
            val intent = Intent(this@MainActivity, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        prefs = getSharedPreferences("profile", MODE_PRIVATE)
    }
}