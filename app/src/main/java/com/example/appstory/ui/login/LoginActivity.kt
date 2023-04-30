package com.example.appstory.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appstory.data.Result
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.appstory.R
import com.example.appstory.databinding.ActivityLoginBinding
import com.example.appstory.ui.custom.EmailEditText
import com.example.appstory.ui.custom.MyButtonLogin
import com.example.appstory.ui.custom.NameEditText
import com.example.appstory.ui.custom.PasswordEditText
import com.example.appstory.ui.main.MainActivity
import com.example.appstory.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
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

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setupAction()
        binding.registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            if (!binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()){
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val result = loginViewModel.loginUser(email,password)

                result.observe(this){
                    when (it) {
                        is Result.Error -> {
                            val error = it.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            val data = it.data
                            Log.d("LoginActivity", "Token: ${data.loginResult.token}")
                        }else ->{

                        }
                    }
                }
            }

        }
    }
}