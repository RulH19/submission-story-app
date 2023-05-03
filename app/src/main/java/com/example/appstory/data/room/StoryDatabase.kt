package com.example.appstory.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StoryEntity::class], version = 1)
abstract class StoryDatabase :RoomDatabase(){
    abstract fun storyDao():StoryDao

    companion object{
        @Volatile
        private var INSTANCE:StoryDatabase ?= null

        fun getInstance(context: Context): StoryDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(context, StoryDatabase::class.java, "stories.db").build()
            }.also { INSTANCE= it }
    }
}