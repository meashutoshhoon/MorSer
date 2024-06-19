package com.jb.morser.ui.enums

import com.jb.morser.ui.util.Preferences

enum class ThemeMode {
    SYSTEM, LIGHT, DARK;

    companion object {
        fun getCurrent() = valueOf(Preferences.getString(Preferences.THEME_MODE, SYSTEM.name))
    }
}