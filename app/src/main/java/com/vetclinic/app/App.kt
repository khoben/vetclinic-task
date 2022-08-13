package com.vetclinic.app

import android.app.Application
import com.vetclinic.app.di.AppContainer
import com.vetclinic.app.di.DiContainer
import timber.log.Timber

open class App : Application() {

    open val appContainer: DiContainer by lazy {
        AppContainer(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}