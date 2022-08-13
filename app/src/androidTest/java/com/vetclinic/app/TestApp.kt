package com.vetclinic.app

import android.util.Log
import com.vetclinic.app.di.DiContainer
import com.vetclinic.app.ui.mock.MockDiContainer

class TestApp : App() {
    override val appContainer: DiContainer = MockDiContainer

    init {
        Log.i("TEST", "Using TestApp")
    }
}