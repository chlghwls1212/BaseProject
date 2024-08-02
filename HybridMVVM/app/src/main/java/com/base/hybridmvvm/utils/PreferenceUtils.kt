package com.base.hybridmvvm.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtils {

    private const val DEFAULT_PREF_NAME = "MainPref"

    private fun getPreferences(context: Context, prefName: String? = null): SharedPreferences {
        val name = prefName ?: DEFAULT_PREF_NAME
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun putString(
        context: Context,
        key: String,
        value: String,
        prefName: String? = DEFAULT_PREF_NAME
    ) {
        val preferences = getPreferences(context, prefName)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(
        context: Context,
        key: String,
        defaultValue: String? = null,
        prefName: String? = DEFAULT_PREF_NAME
    ): String? {
        val preferences = getPreferences(context, prefName)
        return preferences.getString(key, defaultValue)
    }


    fun remove(context: Context, key: String, prefName: String? = null) {
        val preferences = getPreferences(context, prefName)
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clear(context: Context, prefName: String? = null) {
        val preferences = getPreferences(context, prefName)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}
