package com.example.appstory.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appstory.data.repository.StoryAppRepository
import com.example.appstory.di.Injection
import com.example.appstory.ui.addstory.AddStoryViewModel
import com.example.appstory.ui.login.LoginViewModel
import com.example.appstory.ui.map.MapsViewModel
import com.example.appstory.ui.register.RegisterViewModel


class ViewModelFactory private constructor(private val storyAppRepository: StoryAppRepository, ) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyAppRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyAppRepository) as T

            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyAppRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(storyAppRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context :Context): ViewModelFactory = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { INSTANCE = it }
    }
}