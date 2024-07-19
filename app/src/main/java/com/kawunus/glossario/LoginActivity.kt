package com.kawunus.glossario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kawunus.glossario.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun init() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
    }
}