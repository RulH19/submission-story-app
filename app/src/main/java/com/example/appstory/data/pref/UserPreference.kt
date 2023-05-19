package com.example.appstory.data.pref

import android.content.Context

internal class UserPreference(context: Context) {


    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val editor  = preferences?.edit()
        editor?.apply {
            putString(USER_ID, value.userId)
            putString(USER_NAME, value.name)
            putString(USER_TOKEN, value.token)
            putBoolean(USER_IS_LOGIN, value.isLogin!!)
        }
        editor?.apply()
    }

    fun getUser(): User {
        val user = User()
        user.apply {
            userId = preferences?.getString(USER_ID, "")
            name = preferences?.getString(USER_NAME, "")
            token = preferences?.getString(USER_TOKEN, "")
            isLogin = preferences?.getBoolean(USER_IS_LOGIN, false)
        }
        return user
    }
    fun userLogout(){
        val editor  = preferences?.edit()
        editor?.clear()
        editor?.apply()
    }

    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val USER_TOKEN = "user_token"
        private const val USER_IS_LOGIN = "user_is_login"
    }

}