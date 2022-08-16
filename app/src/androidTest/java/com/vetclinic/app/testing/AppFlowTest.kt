package com.vetclinic.app.testing

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.vetclinic.app.testing.idling.EspressoIdlingResource
import com.vetclinic.app.testing.mock.DiContainerConfig
import com.vetclinic.app.testing.mock.MockDiContainer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import kotlin.reflect.KClass

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
abstract class AppFlowTest(
    private val clazz: KClass<out AppCompatActivity>,
    private val amountIdlingResources: Int = 0
) {

    @Before
    open fun before() {
        MockDiContainer.config = DiContainerConfig()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    open fun after() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    fun launchWithIdling(scenarioScope: (ActivityScenario<out AppCompatActivity>) -> Unit) {
        waitForResources(amountIdlingResources)
        launch(scenarioScope)
    }

    fun launch(scenarioScope: (ActivityScenario<out AppCompatActivity>) -> Unit) {
        ActivityScenario.launch(clazz.java, null).use(scenarioScope)
    }

    private fun waitForResources(countResources: Int) {
        repeat(countResources) {
            EspressoIdlingResource.increment()
        }
    }

    inline fun updateDiConfig(crossinline block: DiContainerConfig.() -> DiContainerConfig) {
        MockDiContainer.config = MockDiContainer.config.block()
    }
}