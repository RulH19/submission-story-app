package com.example.appstory.di

import android.content.Context
import com.example.appstory.data.model.ApiConfig
import com.example.appstory.data.repository.StoryAppRepository


object Injection {
    fun provideRepository(context: Context): StoryAppRepository {
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository.getInstance(apiService, context)
    }
}