package com.vetclinic.app

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestAppAndroidTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}