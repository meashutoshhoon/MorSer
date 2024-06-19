package com.jb.morser

import android.app.Application
import com.jb.morser.ui.util.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        applicationScope = CoroutineScope(SupervisorJob())
        Preferences.init(this)
    }

    companion object {
        lateinit var applicationScope: CoroutineScope
    }
}