package com.example.appstory.ui.addstory

import androidx.lifecycle.ViewModel
import com.example.appstory.data.repository.StoryAppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {
    fun uploadNewStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody

    ) = storyAppRepository.postNewStory(photo, description, longitude, latitude )
}