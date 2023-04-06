package com.mh.circutversionupdate.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


object AppStorage {
    private lateinit var prefs: SharedPreferences


    fun init(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getUser(): Boolean {
        return prefs.getBoolean("user_model",false)

    }

    fun setUser(type: Boolean) {
        prefs.edit().putBoolean("user_model", type).apply()
    }

    fun getItemPosition(): Int {
        return prefs.getInt("item",-1)

    }

    fun setItemPosition(type: Int) {
        prefs.edit().putInt("item", type).apply()
    }
    fun clearPreference(){
        prefs.edit().clear().apply()
    }

}