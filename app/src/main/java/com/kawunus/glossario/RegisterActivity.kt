package com.kawunus.glossario

import AccountHelper.AccountHelper
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kawunus.glossario.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    val mAuth = FirebaseAuth.getInstance()
    private val helper = AccountHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        if (intent.getStringExtra("TYPE") == "login") {
            updateUi()
        } else {

            binding.signUpButton.setOnClickListener {
                helper.signUpWithEmail(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString(),
                    binding.birthdayEditText.text.toString()
                )
            }
        }
    }

    private fun init() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
    }

    private fun updateUi() = with(binding) {
        titleTextView.setText(R.string.sign_in_with_email)
        birthdayEditText.visibility = View.GONE
        birthdayTextView.visibility = View.GONE
        forgetPasswordTextView.visibility = View.GONE
        signUpButton.setText(R.string.sign_in)
    }
}