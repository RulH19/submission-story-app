package com.example.appstory.ui.register

import android.service.controls.ControlsProviderService
import android.util.Log
import com.example.appstory.data.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.appstory.data.model.ApiConfig
import com.example.storyapp.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    var responseSignUp = MediatorLiveData<Result<RegisterResponse>>()
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

}