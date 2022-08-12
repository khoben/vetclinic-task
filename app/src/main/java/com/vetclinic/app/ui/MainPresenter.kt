package com.vetclinic.app.ui

import com.vetclinic.app.common.ui.Presenter
import com.vetclinic.app.navigation.Navigation

class MainPresenter(private val navigation: Navigation.Observe) : Presenter {
    val navigationObserver get() = navigation.observe()
}