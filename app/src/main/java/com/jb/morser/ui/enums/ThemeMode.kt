package com.jb.morser.ui.enums

import com.jb.morser.ui.util.Preferences
import com.jb.morser.ui.util.THEME_MODE

enum class ThemeMode {
    SYSTEM, LIGHT, DARK;

    companion object {
        val current: ThemeMode = valueOf(Preferences.getString(THEME_MODE, SYSTEM.name))
    }
}