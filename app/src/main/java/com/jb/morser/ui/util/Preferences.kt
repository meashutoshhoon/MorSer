package com.jb.morser.ui.util

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val PREF_FILE_NAME = "GemiChat"
    private lateinit var prefs: SharedPreferences

    const val THEME_MODE = "themeMode"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    }

    fun edit(action: SharedPreferences.Editor.() -> Unit) {
        prefs.edit().apply(action).apply()
    }

    fun getString(key: String, defValue: String) = prefs.getString(key, defValue) ?: defValue
}