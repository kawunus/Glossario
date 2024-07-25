package com.kawunus.glossario

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kawunus.glossario.data.preferences.UserSharedPreferences

class App : Application() {
    private var darkTheme = false
    private lateinit var userSharedPreferences: UserSharedPreferences

    override fun onCreate() {
        super.onCreate()

        userSharedPreferences = UserSharedPreferences(context = this@App)
        darkTheme = userSharedPreferences.getTheme()
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun switchTheme(darkTheme: Boolean) {
        this.darkTheme = darkTheme
        userSharedPreferences.setTheme(darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}