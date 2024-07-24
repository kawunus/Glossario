package com.kawunus.glossario.accountHelper

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.kawunus.glossario.R
import com.kawunus.glossario.ui.activities.RegisterActivity


class AccountHelper(private val act: RegisterActivity) {
    private lateinit var signInClient: GoogleSignInClient
    private val dataHandler = DataHandler()
    fun signUpWithEmail(
        email: String, password: String, nickname: String
    ) {
        if (email.isNotEmpty() && password.isNotEmpty() && nickname.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        dataHandler.sendRegistrationDataToFirebase(task, nickname, email, "", act)
                    } else {
                        showErrorToast(act.getString(R.string.sign_up_error_firebase))
                    }
                }
        } else {
            showErrorToast(act.getString(R.string.sign_up_error_empty))
        }
    }

    fun signInWithEmail(
        email: String, password: String
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dataHandler.getDataFromFirebase(task, act)
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
            .requestEmail().build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun signInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        act.activityResultLauncher.launch(intent)
    }

    fun signInFirebaseWithGoogle(token: String, account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                dataHandler.sendRegistrationDataToFirebase(
                    task, account.displayName!!, account.email!!, account.photoUrl!!.toString(), act
                )
            } else {
                showErrorToast(act.getString(R.string.sign_in_error_google))
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isNotEmpty()) {
            act.mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
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
        Toast.makeText(act, message, Toast.LENGTH_LONG).show()
    }

}
