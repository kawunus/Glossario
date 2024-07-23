package com.kawunus.glossario

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kawunus.glossario.accountHelper.AccountHelper
import com.kawunus.glossario.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    lateinit var prefs: SharedPreferences
    val mAuth = FirebaseAuth.getInstance()
    private val helper = AccountHelper(this)
    val database = FirebaseDatabase.getInstance().reference

    val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    helper.signInFirebaseWithGoogle(account.idToken!!, account)

                }
            } catch (e: ApiException) {
                Toast.makeText(
                    this@RegisterActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG
                ).show()

            }
        }
    }

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
                    binding.nameEditText.text.toString()
                )
            }

        }
        binding.signUpGoogleButton.setOnClickListener {
            helper.signInWithGoogle()
        }
    }

    private fun init() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        prefs = getSharedPreferences("profile", MODE_PRIVATE)
    }

    private fun updateUi() = with(binding) {
        titleTextView.setText(R.string.sign_in_with_email)
        forgetPasswordTextView.visibility = View.VISIBLE
        signUpButton.setText(R.string.sign_in)
        nameEditText.visibility = View.GONE
        nameTextView.visibility = View.GONE
        toolbar.setTitle(R.string.sign_in_toolbar_title)
    }

}