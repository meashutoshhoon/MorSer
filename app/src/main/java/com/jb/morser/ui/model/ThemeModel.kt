package com.jb.morser.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jb.morser.ui.enums.ThemeMode

class ThemeModel : ViewModel() {
    var themeMode by mutableStateOf(
        ThemeMode.getCurrent()
    )
}