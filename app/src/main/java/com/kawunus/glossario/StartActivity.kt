package com.kawunus.glossario

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kawunus.glossario.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)

        binding.signInButton.setOnClickListener {
            val intent = Intent(this@StartActivity, RegisterActivity::class.java)
            intent.putExtra("TYPE", "login")
            startActivity(intent)

        }

        binding.signUpTextView.setOnClickListener {
            val intent = Intent(this@StartActivity, RegisterActivity::class.java)
            intent.putExtra("TYPE", "register")
            startActivity(intent)

        }
    }

    private fun init() {
        binding = ActivityStartBinding.inflate(layoutInflater)
    }
}