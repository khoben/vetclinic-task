package com.vetclinic.app.navigation

import com.vetclinic.app.common.ui.UiExecutor
import com.vetclinic.app.testing.getOrAwaitValue
import org.junit.Assert
import org.junit.Test

class NavigationTest {
    private val navigation = Navigation.Base(UiExecutor.Test())

    @Test
    fun testNavigation() {
        val expectedScreen = Screen.Test
        navigation.to(expectedScreen)
        Assert.assertEquals(expectedScreen, navigation.observe().getOrAwaitValue())
    }
}