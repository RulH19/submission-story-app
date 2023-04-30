package com.example.appstory.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appstory.R
import com.example.appstory.databinding.ActivityLoginBinding
import com.example.appstory.ui.custom.EmailEditText
import com.example.appstory.ui.custom.MyButtonLogin
import com.example.appstory.ui.custom.NameEditText
import com.example.appstory.ui.custom.PasswordEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var myButtonLogin: MyButtonLogin
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButtonLogin = binding.loginButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

    }

}