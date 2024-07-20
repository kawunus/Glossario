package com.kawunus.glossario.accountHelper

import android.content.Intent
import android.widget.Toast
import androidx.core.content.edit
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.kawunus.glossario.MainActivity
import com.kawunus.glossario.R
import com.kawunus.glossario.RegisterActivity


class AccountHelper(private val act: RegisterActivity) {
    private lateinit var signInClient: GoogleSignInClient

    fun signUpWithEmail(
        email: String,
        password: String,
        birthdayDate: String
    ) {
        if (email.isNotEmpty() && password.isNotEmpty() && birthdayDate.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        confirmAccount(email)
                    } else {
                        showErrorToast(act.getString(R.string.sign_up_error_firebase))
                    }
                }
        } else {
            showErrorToast(act.getString(R.string.sign_up_error_empty))
        }
    }

    fun signInWithEmail(
        email: String,
        password: String
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        confirmAccount(email)
                    } else {
                        showErrorToast(act.getString(R.string.sign_in_error_firebase))
                    }
                }
        } else {
            showErrorToast(act.getString(R.string.sign_in_error_empty))
        }
    }


    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("423705254733-c267q0fc1jobhlut5df1b3f8jm5de1a0.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun signInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        act.startActivityForResult(intent, GoogleConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token: String, email: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                confirmAccount(email)
            } else {
                showErrorToast(act.getString(R.string.sign_in_error_google))
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isNotEmpty()) {
            act.mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showErrorToast(act.getString(R.string.reset_message))
                    } else {
                        showErrorToast(act.getString(R.string.reset_error_firebase))
                    }
                }
        } else {
            showErrorToast(act.getString(R.string.reset_empty))
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(act, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun confirmAccount(email: String) {
        act.prefs.edit(commit = true) {
            putString("status", "register")
            putString("email", email)
        }

        val intent = Intent(act, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        act.startActivity(intent)
        act.finish()
    }
}