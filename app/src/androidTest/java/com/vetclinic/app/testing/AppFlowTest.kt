package com.vetclinic.app.testing

import androidx.test.espresso.IdlingRegistry
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.vetclinic.app.testing.idling.EspressoIdlingResource
import com.vetclinic.app.testing.mock.DiContainerConfig
import com.vetclinic.app.testing.mock.MockDiContainer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
abstract class AppFlowTest {

    @Before
    open fun before() {
        MockDiContainer.config = DiContainerConfig()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    open fun after() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    fun waitForResourcesLoaded(countResources: Int) {
        repeat(countResources) {
            EspressoIdlingResource.increment()
        }
    }

    inline fun updateDiConfig(crossinline block: DiContainerConfig.() -> DiContainerConfig) {
        MockDiContainer.config = MockDiContainer.config.block()
    }
}