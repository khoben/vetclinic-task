package com.vetclinic.app.ui

import com.vetclinic.app.common.observer.SingleEventLiveData
import com.vetclinic.app.navigation.Navigation
import com.vetclinic.app.navigation.Screen
import org.junit.Assert
import org.junit.Test

class MainPresenterTest {

    private val navigation = Navigation.Base()
    private val screen = Screen.Pet("", "")

    private val presenter = MainPresenter(navigation)

    @Test
    fun testNavigation() {
        navigation.to(screen)
        Assert.assertEquals(screen, (presenter.navigationObserver as SingleEventLiveData).lastEmittedData)
    }
}