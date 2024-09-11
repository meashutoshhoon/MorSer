package com.jb.morser.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jb.morser.ui.enums.ThemeMode
import com.jb.morser.ui.util.Preferences
import com.jb.morser.ui.util.THEME_MODE

class ThemeModel : ViewModel() {
    var themeMode by mutableStateOf(ThemeMode.getCurrent())
        private set

    fun updateThemeMode(newMode: ThemeMode) {
        themeMode = newMode
        Preferences.edit {
            putString(THEME_MODE, newMode.name)
        }
    }
}
