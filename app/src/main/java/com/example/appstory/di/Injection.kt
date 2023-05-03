package com.example.appstory.di

import android.content.Context
import com.example.appstory.data.model.ApiConfig
import com.example.appstory.data.repository.StoryRepository
import com.example.appstory.data.repository.UserRepository
import com.example.appstory.data.room.StoryDatabase

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
    fun provideStoryRepository(context: Context) : StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        val dao = database.storyDao()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, dao, appExecutors)
    }
}