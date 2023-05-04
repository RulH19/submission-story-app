package com.example.appstory.data.datastore

import android.media.session.MediaSession.Token
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.appstory.BuildConfig.TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore :DataStore<Preferences>) {
    private val token = stringPreferencesKey("token")
    private val islogin = booleanPreferencesKey("is_login")

    fun getUserToken():Flow<String>{
        return dataStore.data.map{
            it[token] ?:"null"
        }
    }
    fun isLoginFirstTime():Flow<Boolean>{
        return dataStore.data.map {
            it[islogin]?:true
        }
    }
    suspend fun saveUserToken(token:String){
        dataStore.edit{
            it[this.token] = token
        }
    }
    suspend fun userLogin(islogin:Boolean){
        dataStore.edit {
            it[this.islogin] = islogin
        }
    }

    suspend fun userLogout(){
        dataStore.edit {
            it[token] = "null"
        }
    }
    companion object {

        @Volatile
        private var INSTANCE : UserPreference ?= null

        fun getInstance(dataStore:DataStore<Preferences>): UserPreference=
             INSTANCE ?: synchronized(this){
                INSTANCE ?: UserPreference(dataStore)
            }.also { INSTANCE = it }

    }
}