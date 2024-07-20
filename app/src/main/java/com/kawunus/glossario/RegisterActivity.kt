package com.kawunus.glossario

import AccountHelper.AccountHelper
import AccountHelper.GoogleConst
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
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
            binding.signUpButton.setOnClickListener {
                helper.signInWithEmail(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString(),
                )
            }

            binding.forgetPasswordTextView.setOnClickListener {
                helper.resetPassword(binding.emailEditText.text.toString())
            }
        } else {

            binding.signUpButton.setOnClickListener {
                helper.signUpWithEmail(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString(),
                    binding.birthdayEditText.text.toString()
                )
            }

        }
        binding.signUpGoogleButton.setOnClickListener {
            helper.signInWithGoogle()
        }
    }

    private fun init() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
    }

    private fun updateUi() = with(binding) {
        titleTextView.setText(R.string.sign_in_with_email)
        birthdayEditText.visibility = View.GONE
        birthdayTextView.visibility = View.GONE
        forgetPasswordTextView.visibility = View.VISIBLE
        signUpButton.setText(R.string.sign_in)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleConst.GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    helper.signInFirebaseWithGoogle(account.idToken!!)

                }
            } catch (e: ApiException) {
                Toast.makeText(
                    this@RegisterActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG
                ).show()

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}