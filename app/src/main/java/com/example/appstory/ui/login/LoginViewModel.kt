package com.example.appstory.ui.login

import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.lifecycle.*
import com.example.appstory.data.Result
import com.example.appstory.data.datastore.UserPreference
import com.example.appstory.data.model.ApiConfig
import com.example.appstory.data.repository.UserRepository
import com.example.appstory.di.Injection
import com.example.storyapp.model.response.LoginResponse
import com.example.storyapp.model.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val userRepository: UserRepository,
    private val userPreference: UserPreference,
) : ViewModel() {
    fun userLogin(email: String, password: String) = userRepository.loginUser(email, password)

    fun userSaveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreference.saveUserToken(token)
        }
    }

    fun ifFirstTime(): LiveData<Boolean> {
        return userPreference.isLoginFirstTime().asLiveData()
    }

    class LoginViewModelFactory private constructor(
        private val userRepository: UserRepository,
        private val userPreference: UserPreference,
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userRepository, userPreference) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: LoginViewModelFactory? = null
            fun getInstance(
                userPreference: UserPreference,
            ): LoginViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: LoginViewModelFactory(
                        Injection.provideUserRepository(),
                        userPreference
                    )
                }
        }
    }
}