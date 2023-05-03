package com.example.appstory.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appstory.data.datastore.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val userpreference: UserPreference) : ViewModel() {

    fun setFirstTime(firstTime: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userpreference.userLogin(firstTime)
        }
    }

    class MainViewModelFactory private constructor(
        private val userpreference: UserPreference,
    ) :ViewModelProvider.NewInstanceFactory(){
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(userpreference) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        companion object{
            @Volatile
            private var INSTANCE : MainViewModelFactory?=null
            fun getInstance(userpreference: UserPreference): MainViewModelFactory =
                INSTANCE ?: synchronized(this){
                    INSTANCE ?: MainViewModelFactory(userpreference)
                }.also { INSTANCE = it }
        }
    }
}