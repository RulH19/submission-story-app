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

        setMyButtonEnable()
    }
    private fun setMyButtonEnable() {
        val result1 = emailEditText.text
        val result2 = passwordEditText
        myButtonLogin.isEnabled = result1 != null && result1.toString().isNotEmpty() &&
                result2 != null && result2.toString().isNotEmpty()
    }
}