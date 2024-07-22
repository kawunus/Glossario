package com.kawunus.glossario

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kawunus.glossario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)
        replaceFragment(Home())
        if (prefs.getString(
                ProfileKeys.USER_STATUS,
                ProfileKeys.UserStatus.NOT_REGISTER
            ) == ProfileKeys.UserStatus.NOT_REGISTER
        ) {
            val intent = Intent(this@MainActivity, StartActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.home -> replaceFragment(Home())
                R.id.profile -> replaceFragment(Profile())

                else -> {

                }

            }
            true
        }
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        prefs = getSharedPreferences("profile", MODE_PRIVATE)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}