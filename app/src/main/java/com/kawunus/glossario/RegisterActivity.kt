package com.kawunus.glossario

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kawunus.glossario.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        if (intent.getStringExtra("TYPE") == "login")
        {
          updateUi()
        }
        else {

        }
    }

    private fun init() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
    }

    private fun updateUi() = with(binding)
    {
        titleTextView.setText(R.string.sign_in_with_email)
        birthdayEditText.visibility = View.GONE
        birthdayTextView.visibility = View.GONE
        signUpButton.setText(R.string.sign_in)
    }
}