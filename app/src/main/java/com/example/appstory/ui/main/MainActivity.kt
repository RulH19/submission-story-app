package com.example.appstory.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appstory.data.pref.UserPreference
import com.example.appstory.databinding.ActivityMainBinding
import com.example.appstory.ui.home.HomeActivity
import com.example.appstory.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (UserPreference(this).getUser().isLogin == true) {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish()
        }
        binding.button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}