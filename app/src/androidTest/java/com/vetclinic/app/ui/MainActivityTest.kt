package com.vetclinic.app.ui

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.HourDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import com.vetclinic.app.ui.idling.EspressoIdlingResource
import com.vetclinic.app.ui.list.PetListFragment
import com.vetclinic.app.ui.mock.MockDiContainer
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest {

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun checkIfActivityIsLaunched() {
        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.container)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkIfHostFragmentLoaded() {
        launchActivity<MainActivity>().use { scenario ->
            scenario.onActivity { activity ->
                Assert.assertTrue(activity.supportFragmentManager.findFragmentByTag(PetListFragment.TAG) != null)
            }
        }
    }

    @Test
    fun shouldShowDataOnSuccessData() {
        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.working_hours)).check(matches(isDisplayed()))
        onView(withId(com.vetclinic.app.R.id.working_hours)).check(matches(withSubstring("M-F 9:00 - 18:00")))
        onView(withId(com.vetclinic.app.R.id.pet_list)).check(RecyclerViewItemCountAssertion(1))
    }

    @Test
    fun shouldShowErrorOnInvalidData() {

        MockDiContainer.mockedConfigError = true
        MockDiContainer.mockedPetListError = true
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.errors_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldOpenPetOnClickSuccessData() {

        val mockedPetName = "pet"

        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = mockedPetName, contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.pet_list)).check(RecyclerViewItemCountAssertion(1))
        onView(withId(com.vetclinic.app.R.id.pet_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        onView(withId(com.vetclinic.app.R.id.web_view)).check(matches(isDisplayed()))

        // check pet title
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withParent(withResourceName("action_bar"))
            )
        ).check(matches(withText(mockedPetName)))
    }

    @Test
    fun shouldShowAlertOnCallClicked() {

        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.call_btn)).check(matches(isDisplayed()))
        onView(withId(com.vetclinic.app.R.id.call_btn)).perform(click())

        // check alert
        onView(withText(com.vetclinic.app.R.string.alert_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowAlertOnChatClicked() {

        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.chat_btn)).check(matches(isDisplayed()))
        onView(withId(com.vetclinic.app.R.id.chat_btn)).perform(click())

        // check alert
        onView(withText(com.vetclinic.app.R.string.alert_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowWorkingAlertOnChatClickedWithWorkingHours() {

        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedWorkingHours = true
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.chat_btn)).check(matches(isDisplayed()))
        onView(withId(com.vetclinic.app.R.id.chat_btn)).perform(click())

        // check alert
        onView(withText(com.vetclinic.app.R.string.alert_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText(com.vetclinic.app.R.string.alert_working_hours))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowNoWorkingAlertOnChatClickedWithNoWorkingHours() {

        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedWorkingHours = false
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.chat_btn)).check(matches(isDisplayed()))
        onView(withId(com.vetclinic.app.R.id.chat_btn)).perform(click())

        // check alert
        onView(withText(com.vetclinic.app.R.string.alert_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText(com.vetclinic.app.R.string.alert_not_working_hours))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowWorkingAlertOnCallClickedWithWorkingHours() {

        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedWorkingHours = true
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.call_btn)).check(matches(isDisplayed()))
        onView(withId(com.vetclinic.app.R.id.call_btn)).perform(click())

        // check alert
        onView(withText(com.vetclinic.app.R.string.alert_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText(com.vetclinic.app.R.string.alert_working_hours))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowNoWorkingAlertOnCallClickedWithNoWorkingHours() {

        MockDiContainer.mockedConfigError = false
        MockDiContainer.mockedPetListError = false
        MockDiContainer.mockedWorkingHours = false
        MockDiContainer.mockedPetList = listOf(
            PetDomain(imageUrl = "image", title = "pet", contentUrl = "https://google.com")
        )
        MockDiContainer.mockedConfig = ConfigDomain(
            isChatEnabled = true,
            isCallEnabled = true,
            workingHours = WorkingHoursDomain(
                "M-F 9:00 - 18:00",
                HourDomain(2, 9, 0),
                HourDomain(6, 18, 0)
            )
        )

        // wait for fetching
        EspressoIdlingResource.increment()
        EspressoIdlingResource.increment()

        launchActivity<MainActivity>()

        onView(withId(com.vetclinic.app.R.id.call_btn)).check(matches(isDisplayed()))
        onView(withId(com.vetclinic.app.R.id.call_btn)).perform(click())

        // check alert
        onView(withText(com.vetclinic.app.R.string.alert_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText(com.vetclinic.app.R.string.alert_not_working_hours))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }
}