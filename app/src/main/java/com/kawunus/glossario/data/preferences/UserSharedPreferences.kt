package com.kawunus.glossario.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserSharedPreferences(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("profile", Context.MODE_PRIVATE)

    fun setRegisterStatus(status: String) {
        sharedPreferences.edit(commit = true) {
            putString(ProfileKeys.USER_STATUS, status)
        }
    }

    fun setNickname(nickname: String) {
        sharedPreferences.edit(commit = true) {
            putString(ProfileKeys.USER_NICKNAME, nickname)
        }
    }

    fun setEmail(email: String) {
        sharedPreferences.edit(commit = true) {
            putString(ProfileKeys.USER_EMAIL, email)
        }
    }

    fun setImage(image: String) {
        sharedPreferences.edit(commit = true) {
            putString(ProfileKeys.USER_IMAGE, image)
        }
    }

    fun clear() {
        sharedPreferences.edit(commit = true) {
            clear()
        }
    }

    fun getRegisterStatus() =
        sharedPreferences.getString(ProfileKeys.USER_STATUS, ProfileKeys.UserStatus.NOT_REGISTER)

    fun getNickname() =
        sharedPreferences.getString(ProfileKeys.USER_NICKNAME, ProfileKeys.UserStatus.NOT_REGISTER)

    fun getEmail() =
        sharedPreferences.getString(ProfileKeys.USER_EMAIL, ProfileKeys.UserStatus.NOT_REGISTER)

    fun getImage() =
        sharedPreferences.getString(ProfileKeys.USER_IMAGE, ProfileKeys.UserStatus.NOT_REGISTER)


    fun saveUser(email: String, status: String, nickname: String, image: String) {
        setEmail(email)
        setImage(image)
        setRegisterStatus(status)
        setNickname(nickname)
    }

    fun setTheme(darkTheme: Boolean) {
        sharedPreferences.edit(commit = true) {
            putBoolean(ProfileKeys.DARK_THEME, darkTheme)
        }
    }

    fun getTheme(): Boolean = sharedPreferences.getBoolean(ProfileKeys.DARK_THEME, false)
}