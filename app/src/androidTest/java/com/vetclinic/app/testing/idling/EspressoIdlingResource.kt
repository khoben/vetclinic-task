package com.vetclinic.app.testing.idling

import androidx.test.espresso.IdlingResource

/**
 * EspressoIdlingResource.increment()
 *
 * EspressoIdlingResource.safeDecrement()
 */
object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"
    private val countingIdlingResource =
        SimpleCountingIdlingResource(RESOURCE)

    val idlingResource: IdlingResource
        get() = countingIdlingResource

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }

    fun safeDecrement() {
        if (!idlingResource.isIdleNow) {
            decrement()
        }
    }
}