package com.kawunus.glossario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kawunus.glossario.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)

    }

    private fun init(){

    }
}