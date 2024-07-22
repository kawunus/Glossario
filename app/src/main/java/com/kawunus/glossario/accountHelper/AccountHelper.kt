package com.kawunus.glossario.accountHelper

import android.content.Intent
import android.widget.Toast
import androidx.core.content.edit
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.getValue
import com.kawunus.glossario.MainActivity
import com.kawunus.glossario.PrefsKeys
import com.kawunus.glossario.R
import com.kawunus.glossario.RegisterActivity


class AccountHelper(private val act: RegisterActivity) {
    private lateinit var signInClient: GoogleSignInClient

    fun signUpWithEmail(
        email: String, password: String, nickname: String
    ) {
        if (email.isNotEmpty() && password.isNotEmpty() && nickname.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendDataToFirebase(task, nickname, email, "")
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
                    getData(task)
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
        act.startActivityForResult(intent, GoogleConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token: String, account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendDataToFirebase(
                    task, account.displayName!!, account.email!!, account.photoUrl!!.toString()
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


    private fun sendDataToFirebase(
        task: Task<AuthResult>, nickname: String, email: String, imageURL: String
    ) {
        val user = task.result?.user
        user?.let {
            val userId = it.uid
            val userEmail = it.email

            val userMap = mapOf(
                "email" to userEmail, "nickname" to nickname, "profileImage" to imageURL
            )
            act.database.child("users").child(userId).get().addOnCompleteListener { dataTask ->
                if (dataTask.isSuccessful) {
                    if (!dataTask.result.exists()) {

                        act.database.child("users").child(userId).setValue(userMap)
                            .addOnCompleteListener { setTask ->
                                if (setTask.isSuccessful) {
                                    saveData(email, nickname, imageURL)
                                } else {
                                    showErrorToast("Ошибка сохранения данных о пользователе: ${setTask.exception?.message}")
                                }
                            }
                    } else {
                        getData(task)
                    }
                } else {
                    showErrorToast("Ошибка проверки данных о пользователе: ${dataTask.exception?.message}")
                }
            }
        }

    }


    private fun saveData(email: String, nickname: String, imageURL: String) {
        act.prefs.edit(commit = true) {
            putString(PrefsKeys.ProfileKeys.USER_STATUS, PrefsKeys.ProfileKeys.UserStatus.REGISTER)
            putString(PrefsKeys.ProfileKeys.USER_EMAIL, email)
            putString(PrefsKeys.ProfileKeys.USER_NICKNAME, nickname)
            putString(PrefsKeys.ProfileKeys.USER_IMAGE, imageURL)
        }

        val intent = Intent(act, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        act.startActivity(intent)
        act.finish()
    }

    private fun getData(task: Task<AuthResult>) {
        val user = task.result.user
        user?.let {
            val userId = it.uid

            act.database.child("users").child(userId).get().addOnCompleteListener { dataTask ->
                if (dataTask.isSuccessful) {
                    val userData = dataTask.result?.getValue<Map<String, String>>()
                    val userEmail = userData!!["email"] as String
                    val userNickname = userData["nickname"] as String
                    val imageUrl = userData["profileImage"] as String

                    saveData(userEmail, userNickname, imageUrl)
                } else {
                    showErrorToast("Произошла ошибка при получении данных")
                }
            }
        }
    }
}
