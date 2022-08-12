package com.vetclinic.app.ui

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
        presenter.navigationObserver.observe {
            Assert.assertEquals(screen, it)
        }
        navigation.to(screen)
    }
}