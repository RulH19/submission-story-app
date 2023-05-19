package com.example.appstory.ui.login

import androidx.lifecycle.ViewModel
import com.example.appstory.data.repository.StoryAppRepository

class LoginViewModel(private val storyAppRepository: StoryAppRepository): ViewModel() {
    fun sendLogin(email:String, password:String) = storyAppRepository.loginUser(email, password)
}