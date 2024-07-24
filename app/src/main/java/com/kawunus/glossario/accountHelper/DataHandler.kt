package com.kawunus.glossario.accountHelper

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.core.content.edit
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.getValue
import com.kawunus.glossario.ui.activities.MainActivity
import com.kawunus.glossario.ProfileKeys
import com.kawunus.glossario.R
import com.kawunus.glossario.ui.activities.RegisterActivity

class DataHandler {

    fun sendRegistrationDataToFirebase(
        task: Task<AuthResult>,
        nickname: String,
        email: String,
        imageURL: String,
        act: RegisterActivity
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
                                    saveDataToPrefs(email, nickname, imageURL, act)
                                } else {
                                    showErrorToast(
                                        act.getString(
                                            R.string.sign_up_error_save, setTask.exception?.message
                                        ), act
                                    )
                                }
                            }
                    } else {
                        getDataFromFirebase(task, act)
                    }
                } else {
                    showErrorToast(
                        act.getString(
                            R.string.sign_up_error_check, dataTask.exception?.message
                        ), act
                    )
                }
            }
        }
    }

    fun saveDataToPrefs(email: String, nickname: String, imageUrl: String, act: RegisterActivity) {
        act.prefs.edit(commit = true) {
            putString(ProfileKeys.USER_STATUS, ProfileKeys.UserStatus.REGISTER)
            putString(ProfileKeys.USER_EMAIL, email)
            putString(ProfileKeys.USER_NICKNAME, nickname)
            putString(ProfileKeys.USER_IMAGE, imageUrl)
        }

        val intent = Intent(act, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        act.startActivity(intent)
        act.finish()
    }

    fun getDataFromFirebase(task: Task<AuthResult>, act: RegisterActivity) {
        val user = task.result.user
        user?.let {
            val userId = it.uid

            act.database.child("users").child(userId).get().addOnCompleteListener { dataTask ->
                if (dataTask.isSuccessful) {
                    val userData = dataTask.result?.getValue<Map<String, String>>()
                    val userEmail = userData!!["email"] as String
                    val userNickname = userData["nickname"] as String
                    val imageUrl = userData["profileImage"] as String

                    saveDataToPrefs(userEmail, userNickname, imageUrl, act)
                } else {
                    showErrorToast(act.getString(R.string.sign_up_error_get), act)
                }
            }
        }
    }

    private fun showErrorToast(message: String, act: Activity) {
        Toast.makeText(act, message, Toast.LENGTH_LONG).show()
    }
}