package com.example.appstory.di

import com.example.appstory.data.model.ApiConfig
import com.example.appstory.data.repository.UserRepository

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}