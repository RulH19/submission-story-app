package com.example.appstory.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appstory.databinding.ActivityRegisterBinding
import com.example.appstory.ui.custom.EmailEditText

import com.example.appstory.ui.custom.MyButtonRegister
import com.example.appstory.ui.custom.NameEditText
import com.example.appstory.ui.custom.PasswordEditText

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var myButtonRegister: MyButtonRegister
    private lateinit var nameEditText: NameEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButtonRegister = binding.registerButton
        nameEditText = binding.nameEditText
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        setMyButtonEnable()
    }
    private fun setMyButtonEnable() {
        val name = nameEditText.text
        val email = emailEditText.text
        val password = passwordEditText
        myButtonRegister.isEnabled = email != null && email.toString().isNotEmpty() &&
                password != null && password.toString().isNotEmpty() &&
                name != null && name.toString().isNotEmpty()
    }
}