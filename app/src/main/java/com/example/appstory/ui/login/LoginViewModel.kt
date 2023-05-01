package com.example.appstory.ui.login

import android.service.controls.ControlsProviderService
import android.util.Log
import com.example.appstory.data.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.appstory.data.model.ApiConfig
import com.example.storyapp.model.response.LoginResponse
import com.example.storyapp.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var responseLogin = MediatorLiveData<Result<LoginResponse>>()
    fun loginUser(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> {
        responseLogin.value = Result.Loading
        val client = ApiConfig.getApiService().loginUser(email, password)
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


}