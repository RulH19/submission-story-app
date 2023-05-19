package com.example.appstory.ui.map

import androidx.lifecycle.ViewModel
import com.example.appstory.data.repository.StoryAppRepository

class MapsViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {
    fun getMapsStories() = storyAppRepository.getStoriesMap()
}