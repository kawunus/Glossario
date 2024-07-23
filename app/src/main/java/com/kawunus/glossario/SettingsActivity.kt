package com.kawunus.glossario

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        binding.shareView.setOnClickListener{
            binding.shareView.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                val message = getString(R.string.settings_share_message)
                intent.putExtra(Intent.EXTRA_TEXT, message)
                intent.type = "text/plain"
                val chooser = Intent.createChooser(intent, getString(R.string.settings_share_title))
                startActivity(chooser)
            }
        }
        binding.supportView.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))
            startActivity(intent)
        }
        binding.telegramView.setOnClickListener{
            val url = Uri.parse(getString(R.string.settings_chanel))
            val intent = Intent(Intent.ACTION_VIEW, url)

            startActivity(intent)
        }
        binding.gitView.setOnClickListener{
            val url = Uri.parse(getString(R.string.settings_git))
            val intent = Intent(Intent.ACTION_VIEW, url)

            startActivity(intent)
        }

    }

    fun init() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
    }
}