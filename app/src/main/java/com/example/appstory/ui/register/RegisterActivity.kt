package com.example.appstory.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.appstory.databinding.ActivityRegisterBinding
import com.example.appstory.data.Result
import com.example.appstory.ui.login.LoginActivity
import androidx.activity.viewModels
import com.example.appstory.R
import com.example.appstory.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.registerButton.setOnClickListener {
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
            val viewModel: RegisterViewModel by viewModels {
                factory
            }
            val password = binding.passwordEditText.text.toString()
            if (password.length >= 8 && !binding.nameEditText.text.isNullOrEmpty() && !binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()) {
                val name = binding.nameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                viewModel.userRegister(name, email, password).observe(this) {
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
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                this,
                                "Register Berhasil, Login Yukk!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                Toast.makeText(this@RegisterActivity, R.string.minimal_password, Toast.LENGTH_SHORT)
                    .show()
                if (binding.nameEditText.text.isNullOrEmpty()) binding.emailEditText.error =
                    getString(
                        R.string.name_tidak_kosong
                    )
                if (binding.emailEditText.text.isNullOrEmpty()) binding.emailEditText.error =
                    getString(
                        R.string.email_tidak_kosong
                    )
                if (binding.passwordEditText.text.isNullOrEmpty()) binding.passwordEditText.error =
                    getString(
                        R.string.password_minimal
                    )
            }
        }
    }

}