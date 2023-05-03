package com.example.appstory.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.appstory.data.model.ApiService
import com.example.appstory.data.room.StoryDao
import com.example.appstory.data.room.StoryEntity
import com.example.appstory.data.Result
import com.example.appstory.di.AppExecutors
import com.example.storyapp.model.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDao: StoryDao,
    private val appExecutor: AppExecutors
){
    private val getAllStoriesResult = MediatorLiveData<Result<List<StoryEntity>>>()
    fun getAllStories(token:String) :LiveData<Result<List<StoryEntity>>>{
        getAllStoriesResult.value = Result.Loading

        val client = apiService.getStories(token)
        client.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if(response.isSuccessful){
                    val stories = response.body()?.listStory
                    val storiesList = ArrayList<StoryEntity>()

                    appExecutor.diskIO.execute{
                        stories?.forEach {
                            val story = StoryEntity(
                                it.photoUrl,
                                it.createdAt,
                                it.name,
                                it.description,
                                it.lon,
                                it.id,
                                it.lat
                            )
                            storiesList.add(story)
                        }

                        storyDao.deleteAllStories()
                        storyDao.insertStories(storiesList)
                    }
                }else{

                    Log.e(TAG, "Failed: Response Unsuccessful - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                getAllStoriesResult.value = Result.Error("Gagal Memuat Stories")
                Log.e(TAG, "Failed: Response Unsuccessful - ${t.message.toString()}")
            }

        })
        val localData = storyDao.getAllStories()
        getAllStoriesResult.addSource(localData) {
            getAllStoriesResult.value = Result.Success(it)
        }
        return getAllStoriesResult
    }
    companion object {
        private val TAG = StoryRepository::class.java.simpleName
        @Volatile
        private var INSTANCE : StoryRepository ?= null

        fun getInstance(
            apiService: ApiService,
            storyDao: StoryDao,
            appExecutor: AppExecutors
        ): StoryRepository =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: StoryRepository(apiService, storyDao, appExecutor)
            }.also { INSTANCE = it }
    }
}
