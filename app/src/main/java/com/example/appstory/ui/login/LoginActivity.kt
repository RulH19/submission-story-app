package com.example.appstory.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appstory.data.Result
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.appstory.R
import com.example.appstory.data.datastore.UserPreference
import com.example.appstory.databinding.ActivityLoginBinding
import com.example.appstory.ui.custom.EmailEditText
import com.example.appstory.ui.custom.MyButtonLogin
import com.example.appstory.ui.custom.NameEditText
import com.example.appstory.ui.custom.PasswordEditText
import com.example.appstory.ui.home.HomeActivity
import com.example.appstory.ui.main.MainActivity
import com.example.appstory.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginViewModelFactory.getInstance(
            UserPreference.getInstance(dataStore)
        )
    }
    private lateinit var myButtonLogin: MyButtonLogin
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myButtonLogin = binding.loginButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        setupAction()

    }
    override fun onResume() {
        super.onResume()
        initialCheck()
    }
    private fun initialCheck() {
        loginViewModel.ifFirstTime().observe(this) {
            if (it) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.setOnClickListener {

            if (!binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()){
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val result = loginViewModel.userLogin(email,password)

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
                            binding.progressBar.visibility = View.INVISIBLE
                            val data = it.data
                            loginViewModel.userSaveToken(data.loginResult.token)
                            Log.d("LoginActivity", "Token: ${data.loginResult.token}")
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }else {
                if (binding.emailEditText.text.isNullOrEmpty()) binding.emailEditText.error = getString(R.string.email_tidak_kosong)
                if (binding.passwordEditText.text.isNullOrEmpty()) binding.passwordEditText.error = getString(R.string.password_minimal)
            }

        }
    }
}