package com.example.appstory.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getAllStories(): LiveData<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStories(stories: List<StoryEntity>)

    @Query("DELETE FROM story")
    fun deleteAllStories()
}