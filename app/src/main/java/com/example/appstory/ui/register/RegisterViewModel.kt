package com.example.appstory.ui.register

import androidx.lifecycle.ViewModel
import com.example.appstory.data.repository.StoryAppRepository

class RegisterViewModel(private val userRepository: StoryAppRepository): ViewModel() {
    fun userRegister(name :String, email:String, password:String) = userRepository.createUser(name, email, password)
}