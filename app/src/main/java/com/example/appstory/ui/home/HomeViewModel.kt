package com.example.appstory.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.appstory.data.repository.StoryAppRepository
import com.example.appstory.di.Injection
import com.example.storyapp.model.response.ListStoryItem

class HomeViewModel( storyAppRepository: StoryAppRepository, context: Context): ViewModel() {
    val story : LiveData<PagingData<ListStoryItem>> = storyAppRepository.getStory().cachedIn(viewModelScope)

}
class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideRepository(context), context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}