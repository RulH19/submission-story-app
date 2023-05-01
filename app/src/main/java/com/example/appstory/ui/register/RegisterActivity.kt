package com.example.appstory.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.appstory.data.Result
import com.example.appstory.databinding.ActivityRegisterBinding
import com.example.appstory.ui.custom.EmailEditText
import com.example.appstory.ui.custom.MyButtonRegister
import com.example.appstory.ui.custom.NameEditText
import com.example.appstory.ui.custom.PasswordEditText
import com.example.appstory.ui.login.LoginActivity
import com.example.appstory.ui.login.LoginViewModel
import com.example.appstory.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
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

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        setupAction()


    }
    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.registerButton.setOnClickListener {
            if (!binding.nameEditText.text.isNullOrEmpty() && !binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()){
                val name = binding.nameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val result = registerViewModel.createUser(name,email,password)

                result.observe(this){
                    when (it) {
                        is Result.Loading ->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            val error = it.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            Toast.makeText(this, "register berhasil silahkan login", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.INVISIBLE
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else ->{
                    }
                    }
                }
            }

        }
    }

}