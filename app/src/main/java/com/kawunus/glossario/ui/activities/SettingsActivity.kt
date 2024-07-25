package com.kawunus.glossario.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kawunus.glossario.App
import com.kawunus.glossario.R
import com.kawunus.glossario.data.preferences.UserSharedPreferences
import com.kawunus.glossario.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var userSharedPreferences: UserSharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareView.setOnClickListener {
            binding.shareView.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                val message = getString(R.string.settings_share_message)
                intent.putExtra(Intent.EXTRA_TEXT, message)
                intent.type = "text/plain"
                val chooser = Intent.createChooser(intent, getString(R.string.settings_share_title))
                startActivity(chooser)
            }
        }
        binding.supportView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))
            startActivity(intent)
        }
        binding.telegramView.setOnClickListener {
            val url = Uri.parse(getString(R.string.settings_chanel))
            val intent = Intent(Intent.ACTION_VIEW, url)

            startActivity(intent)
        }
        binding.gitView.setOnClickListener {
            val url = Uri.parse(getString(R.string.settings_git))
            val intent = Intent(Intent.ACTION_VIEW, url)

            startActivity(intent)
        }

        binding.logOutView.setOnClickListener {
            logOut()
        }

        binding.deleteAccountView.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                user.delete()
                val userStorageRef =
                    FirebaseStorage.getInstance().reference.child("images/${user.uid}/")
                userStorageRef.delete()
                val userDBRef = FirebaseDatabase.getInstance().reference.child("users/${user.uid}/")
                userDBRef.removeValue()
                logOut()
            }
        }

        binding.themeSwitcher.isChecked = userSharedPreferences.getTheme()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }

    private fun logOut() {
        val userSharedPreferences = UserSharedPreferences(this@SettingsActivity)
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        userSharedPreferences.clear()
        finishAffinity()
    }

    private fun init() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        userSharedPreferences = UserSharedPreferences(this@SettingsActivity)
    }

}