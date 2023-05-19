package com.example.appstory.data.repository

import android.content.Context
import android.os.Build
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.appstory.data.model.ApiConfig
import com.example.appstory.data.model.ApiService
import com.example.storyapp.model.response.LoginResponse
import com.example.storyapp.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import  com.example.appstory.data.Result
import com.example.appstory.data.paging.StoryPagingSource
import com.example.appstory.data.pref.User
import com.example.appstory.data.pref.UserPreference
import com.example.appstory.data.response.AddStoryResponse
import com.example.storyapp.model.response.ListStoryItem
import com.example.storyapp.model.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


class StoryAppRepository private constructor(
    private val apiService: ApiService,
    private val context: Context,
) {
    private val userPreference = UserPreference(context)
    private val responseLogin = MediatorLiveData<Result<String>>()
    private val responseRegister = MediatorLiveData<Result<String>>()
    private val postNewStory = MediatorLiveData<Result<String>>()
    private val getMapStories = MediatorLiveData<Result<List<ListStoryItem>>>()


    fun loginUser(
        email: String,
        password: String,
    ): LiveData<Result<String>> {
        responseLogin.value = Result.Loading
        val client = apiService.loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>,
            ) {
                if (response.isSuccessful) {
                    val loginInfo = response.body()
                    val loginResult = loginInfo?.loginResult
                    if (loginInfo != null) {
                        userPreference.setUser(
                            User(
                                true,
                                loginResult?.name,
                                loginResult?.userId,
                                loginResult?.token
                            )
                        )
                    }
                    responseLogin.value = Result.Success(loginInfo?.message.toString().uppercase())
                } else {
                    responseLogin.value = Result.Error("Login gagal Cek Email dan Password")
                    Log.e(
                        TAG,
                        "Failed: Response Unsuccessful - ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Cek salah", "onFailure: ${t.message}")
            }
        })
        return responseLogin
    }

    fun createUser(
        name: String,
        email: String,
        password: String,
    ): LiveData<Result<String>> {
        responseRegister.value = Result.Loading
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                if (response.isSuccessful) {
                    val registerInfo = response.body()
                    if (registerInfo != null) {
                        responseRegister.value =
                            Result.Success(registerInfo?.message.toString().uppercase())
                    } else {
                        responseRegister.value =
                            Result.Error(registerInfo?.message.toString().uppercase())
                    }
                } else {
                    responseRegister.value = Result.Error("Register failed, email sudah ada")
                    Log.e(
                        TAG,
                        "Failed: Response Unsuccessful - ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Cek salah", "onFailure: ${t.message}")
            }
        })
        return responseRegister
    }

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, context)
            }
        ).liveData
    }

    fun postNewStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
    ): LiveData<Result<String>> {
        postNewStory.value = Result.Loading
        apiService.postStory(
            "Bearer ${UserPreference(context).getUser().token}",
            description, photo, latitude, longitude
        ).enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>,
            ) {
                if (response.isSuccessful) {
                    val message = response.body()?.message.toString()
                    postNewStory.value = Result.Success(message)
                } else {
                    val error = "Gagal Memuat Cerita"
                    postNewStory.value = Result.Error(error)
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                postNewStory.value = Result.Error("Gagal Memuat Stories")
                Log.e(TAG, "Failed: Response Unsuccessful - ${t.message.toString()}")
            }

        })
        return postNewStory
    }

    fun getStoriesMap(): LiveData<Result<List<ListStoryItem>>> {
        getMapStories.value = Result.Loading
        val client =
            apiService.getStoriesLocations("Bearer ${UserPreference(context).getUser().token}", 1)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val mapStories = response.body()?.listStory
                    if (mapStories != null) {
                        getMapStories.value = Result.Success(mapStories)
                    }
                } else {
                    val error = "Gagal mengambil Lokasi"
                    getMapStories.value = Result.Error(error)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                getMapStories.value = Result.Error("Gagal mengambil Lokasi")
            }
        })
        return getMapStories
    }

    companion object {

        @Volatile
        private var INSTANCE: StoryAppRepository? = null


        fun getInstance(apiService: ApiService, context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryAppRepository(apiService, context)
            }.also { INSTANCE = it }
    }
}