package com.example.easyfindapp.user_preference

import android.content.Context
import com.example.easyfindapp.App

object UserPreference {
    const val USER_ID = "User ID"

    private val preference by lazy {
        App.appInstance!!.applicationContext.getSharedPreferences("User",Context.MODE_PRIVATE)
    }
    private val editor by lazy {
        preference.edit()
    }
    fun saveData(key : String, value : String) {
        editor.putString(key,value)
        editor.commit()
    }
    fun getData(key : String) = preference.getString(key, "")

    fun removeData(key : String) {
        if (preference.contains(key)) {
            editor.remove(key)
        }
    }
    fun clearAllData() {
        editor.clear()
        editor.commit()
    }
}