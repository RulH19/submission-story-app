package com.example.appstory.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.appstory.R
import com.example.appstory.data.Result
import com.example.appstory.databinding.ActivityRegisterBinding
import com.example.appstory.ui.custom.EmailEditText
import com.example.appstory.ui.custom.MyButtonRegister
import com.example.appstory.ui.custom.NameEditText
import com.example.appstory.ui.custom.PasswordEditText
import com.example.appstory.ui.login.LoginActivity
import androidx.activity.viewModels

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModel.RegisterViewModelFactory.getInstance()
    }
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

        setupAction()


    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (!binding.nameEditText.text.isNullOrEmpty() && !binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()) {
                val result = registerViewModel.registerUser(name, email, password)
                result.observe(this) {
                    when (it) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            val error = it.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            Toast.makeText(
                                this,
                                "register berhasil silahkan login",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = View.INVISIBLE
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }else {
                if (name.isNullOrEmpty()) binding.nameEditText.error = getString(R.string.name_tidak_kosong)
                if (email.isNullOrEmpty()) binding.emailEditText.error = getString(R.string.email_tidak_kosong)
                if (password.isNullOrEmpty()) binding.passwordEditText.error = getString(R.string.password_minimal)
            }

        }
    }

}