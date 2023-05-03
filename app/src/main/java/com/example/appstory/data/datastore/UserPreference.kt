package com.example.appstory.data.datastore

import android.media.session.MediaSession.Token
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore :DataStore<Preferences>) {


    fun getUserToken():Flow<String>{
        return dataStore.data.map{
            it[TOKEN] ?:"null"
        }
    }
    fun isLoginFirstTime():Flow<Boolean>{
        return dataStore.data.map {
            it[ISLOGIN]?:true
        }
    }
    suspend fun saveUserToken(token:String){
        dataStore.edit{
            it[TOKEN] = token
        }
    }
    suspend fun userLogin(firstTime:Boolean){
        dataStore.edit {
            it[ISLOGIN] = firstTime
        }
    }

    suspend fun userLogout(){
        dataStore.edit {
            it[TOKEN] = "null"
        }
    }
    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val ISLOGIN = booleanPreferencesKey("is_login")
        @Volatile
        private var INSTANCE : UserPreference ?= null

        fun getInstance(dataStore:DataStore<Preferences>): UserPreference{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: UserPreference(dataStore)
            }.also { INSTANCE = it }
        }
    }
}