package com.kawunus.glossario.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kawunus.glossario.R
import com.kawunus.glossario.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
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
    }

    private fun logOut() {
        val prefs: SharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
        prefs.edit(commit = true) {
            clear()
            finishAffinity()
        }
    }

    private fun init() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
    }

    private fun deleteFolder(ref: StorageReference) {
        ref.listAll().addOnSuccessListener { listResult ->
            for (fileRef in listResult.items) {
                fileRef.delete().addOnSuccessListener {
                    Log.d(
                        "DeleteFile", "File deleted successfully"
                    )
                }.addOnFailureListener { exception ->
                    Log.e(
                        "DeleteFile", "Error deleting file", exception
                    )
                }
            }
            for (folderRef in listResult.prefixes) {
                deleteFolder(folderRef)
            }
        }.addOnFailureListener { exception ->
            Log.e(
                "ListAll", "Error listing items", exception
            )
        }
    }
}