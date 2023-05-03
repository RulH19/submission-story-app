package com.example.appstory.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.example.appstory.data.datastore.UserPreference
import com.example.appstory.data.repository.StoryRepository
import com.example.appstory.di.Injection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference
) : ViewModel(){
    fun getAllStories(token:String) = storyRepository.getAllStories(token)

    fun checkAvailableToken():LiveData<String>{
        return userPreference.getUserToken().asLiveData()
    }
    fun userLogout(){
        viewModelScope.launch (Dispatchers.IO){
            userPreference.userLogout()
        }
    }

    class HomeViewModelFactory private constructor(
        private val storyRepository: StoryRepository,
        private val userPreference: UserPreference
    ):ViewModelProvider.NewInstanceFactory(){
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
                return HomeViewModel(storyRepository, userPreference) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        companion object {
           @Volatile
           private var INSTANCE :HomeViewModelFactory ?= null
           fun getInstance(
               context :Context,
               userPreference: UserPreference
           ) :HomeViewModelFactory = INSTANCE ?: synchronized(this){
               INSTANCE ?: HomeViewModelFactory(
                   Injection.provideStoryRepository(context), userPreference
               )
           }.also { INSTANCE = it }
        }
    }
}