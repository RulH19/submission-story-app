package com.example.appstory.data.model

import com.example.appstory.data.response.AddStoryResponse
import com.example.storyapp.model.response.LoginResponse
import com.example.storyapp.model.response.RegisterResponse
import com.example.storyapp.model.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): StoryResponse

    @GET("stories")
    fun getStoriesLocations(
        @Header("Authorization") token: String,
        @Query("location") location: Int? = null,
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") authToken: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody,
    ): Call<AddStoryResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>
}