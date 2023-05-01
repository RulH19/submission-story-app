package com.example.appstory.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore :DataStore<Preferences>) {
    private val token = stringPreferencesKey("token")
    private val isLogin = booleanPreferencesKey("is_login")

    fun getUserToken():Flow<String>{
        return dataStore.data.map{
            it[token] ?:""
        }
    }
    fun isLoginFirstTime():Flow<Boolean>{
        return dataStore.data.map {
            it[isLogin]?:true
        }
    }
    suspend fun saveUserToken(token:String){
        dataStore.edit{
            it[this.token] = token
        }
    }
    suspend fun userLogin(firstTime:Boolean){
        dataStore.edit {
            it[this.isLogin] = firstTime
        }
    }

    suspend fun userLogout(){
        dataStore.edit {
            it[token] = ""
        }
    }
    companion object {
        @Volatile
        private var INSTANCE : UserPreference ?= null

        fun getInstance(dataStore:DataStore<Preferences>): UserPreference{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}