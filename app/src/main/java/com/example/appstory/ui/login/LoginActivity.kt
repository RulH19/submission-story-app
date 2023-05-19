package com.example.appstory.ui.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.appstory.data.Result
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.appstory.R
import com.example.appstory.data.pref.UserPreference
import com.example.appstory.databinding.ActivityLoginBinding
import com.example.appstory.ui.ViewModelFactory
import com.example.appstory.ui.home.HomeActivity
import com.example.appstory.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (UserPreference(this).getUser().isLogin == true) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        setupAction()

    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.setOnClickListener {
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
            val viewModel: LoginViewModel by viewModels { factory }
            val password = binding.passwordEditText.text.toString()
            if (password.length >= 8 && !binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()) {
                val email = binding.emailEditText.text.toString()

                viewModel.sendLogin(email, password).observe(this) {
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
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }

            } else {
                Toast.makeText(this@LoginActivity, R.string.minimal_password, Toast.LENGTH_SHORT)
                    .show()
                if (binding.emailEditText.text.isNullOrEmpty()) binding.emailEditText.error =
                    getString(
                        R.string.email_tidak_kosong
                    )
                if (binding.passwordEditText.text.isNullOrEmpty()) binding.passwordEditText.error =
                    getString(R.string.password_minimal)
            }
        }
    }
}