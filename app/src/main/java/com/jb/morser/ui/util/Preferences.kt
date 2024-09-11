package com.jb.morser.ui.util

import android.content.Context
import android.content.SharedPreferences

const val THEME_MODE = "themeMode"

object Preferences {
    private const val PREF_FILE_NAME = "MorSer"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun edit(action: SharedPreferences.Editor.() -> Unit) {
        prefs.edit().apply(action).apply()
    }

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue) ?: defValue
    }
}