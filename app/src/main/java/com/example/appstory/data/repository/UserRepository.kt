package com.example.appstory.data.repository

import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.appstory.data.Result
import com.example.appstory.data.model.ApiConfig
import com.example.appstory.data.model.ApiService
import com.example.storyapp.model.response.LoginResponse
import com.example.storyapp.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService
) {
    var responseLogin = MediatorLiveData<Result<LoginResponse>>()
    var responseSignUp = MediatorLiveData<Result<RegisterResponse>>()

    fun loginUser(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> {
        responseLogin.value = Result.Loading
        val client = apiService.loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>,
            ) {
                if (response.isSuccessful) {
                    val loginInfo = response.body()
                    if (loginInfo != null) {
                        responseLogin.value = Result.Success(loginInfo)
                    }
                } else {
                    responseLogin.value = Result.Error("Login gagal Cek Email dan Password")
                    Log.e(ControlsProviderService.TAG, "Failed: Response Unsuccessful - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Cek salah", "onFailure: ${t.message}")
            }
        })
        return responseLogin
    }

    fun createUser(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                if (response.isSuccessful) {
                    val registerInfo = response.body()
                    if (registerInfo != null) {
                        responseSignUp.value = Result.Success(registerInfo)

                    }
                }else {
                    responseSignUp.value = Result.Error("Register failed, email sudah ada")
                    Log.e(ControlsProviderService.TAG, "Failed: Response Unsuccessful - ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Cek salah", "onFailure: ${t.message}")
            }
        })
        return responseSignUp
    }

    companion object {

        @Volatile
        private var INSTANCE: UserRepository? = null


        fun getInstance(apiService: ApiService) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(apiService)
            }.also { INSTANCE = it }
    }
}