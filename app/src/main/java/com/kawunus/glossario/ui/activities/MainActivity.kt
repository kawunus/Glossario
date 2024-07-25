package com.kawunus.glossario.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kawunus.glossario.data.preferences.ProfileKeys
import com.kawunus.glossario.R
import com.kawunus.glossario.data.preferences.UserSharedPreferences
import com.kawunus.glossario.databinding.ActivityMainBinding
import com.kawunus.glossario.ui.fragments.HomeFragment
import com.kawunus.glossario.ui.fragments.LibraryFragment
import com.kawunus.glossario.ui.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userSharedPreferences: UserSharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        if (userSharedPreferences.getRegisterStatus() == ProfileKeys.UserStatus.NOT_REGISTER
        ) {
            val intent = Intent(this@MainActivity, StartActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.home -> replaceFragment(HomeFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.library -> replaceFragment(LibraryFragment())
                else -> {

                }

            }
            true
        }
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        userSharedPreferences = UserSharedPreferences(this@MainActivity)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}