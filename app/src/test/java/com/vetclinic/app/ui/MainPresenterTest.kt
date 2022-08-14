package com.vetclinic.app.ui

import com.vetclinic.app.common.ui.UiExecutor
import com.vetclinic.app.navigation.Navigation
import com.vetclinic.app.navigation.Screen
import com.vetclinic.app.testing.getOrAwaitValue
import org.junit.Assert
import org.junit.Test

class MainPresenterTest {

    @Test
    fun testScreenNavigation() {
        val navigation = Navigation.Base(UiExecutor.Test())
        val presenter = MainPresenter(navigation)
        val expectedScreen = Screen.Test

        navigation.to(expectedScreen)

        Assert.assertEquals(expectedScreen, presenter.navigationObserver.getOrAwaitValue())
    }
}