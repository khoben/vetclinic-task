package com.vetclinic.app

import android.app.Application
import com.vetclinic.app.di.AppContainer
import timber.log.Timber

class App : Application() {
    val appContainer by lazy { AppContainer(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}