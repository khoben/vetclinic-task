package com.vetclinic.app.ui

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.HourDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import com.vetclinic.app.testing.AppFlowTest
import com.vetclinic.app.testing.viewassertions.RecyclerViewItemCountAssertion
import com.vetclinic.app.ui.list.PetListFragment
import com.vetclinic.app.ui.pet.PetFragment
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Test


class MainActivityTest : AppFlowTest() {

    @Test
    fun checkIfActivityIsLaunched() {
        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.container)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkIfPetListFragmentPresentedOnStart() {
        launchActivity<MainActivity>().use { scenario ->
            scenario.onActivity { activity ->
                Assert.assertTrue(
                    activity.supportFragmentManager.findFragmentByTag(PetListFragment.TAG) != null
                )
            }
        }
    }

    @Test
    fun shouldShowDataOnSuccessData() {

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.working_hours)).check(matches(isDisplayed()))
            onView(withId(com.vetclinic.app.R.id.working_hours)).check(matches(withSubstring("M-F 9:00 - 18:00")))
            onView(withId(com.vetclinic.app.R.id.pet_list)).check(RecyclerViewItemCountAssertion(1))
        }
    }

    @Test
    fun shouldShowErrorOnInvalidBothData() {
        updateDiConfig {
            copy(
                isConfigError = true,
                isPetListError = true
            )
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.errors_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldShowErrorOnInvalidConfigData() {
        updateDiConfig {
            copy(
                isConfigError = true
            )
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.errors_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldShowErrorOnInvalidPetListData() {
        updateDiConfig {
            copy(
                isPetListError = true
            )
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.errors_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldReloadDataOnRetry() {
        updateDiConfig {
            copy(
                isPetListError = true,
                isConfigError = true
            )
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.errors_layout)).check(matches(isDisplayed()))

            updateDiConfig {
                copy(
                    isPetListError = false,
                    isConfigError = false
                )
            }

            onView(withId(com.vetclinic.app.R.id.retry)).perform(click())

            onView(withId(com.vetclinic.app.R.id.errors_layout)).check(matches(not(isDisplayed())))
            onView(withId(com.vetclinic.app.R.id.working_hours)).check(matches(isDisplayed()))
            onView(withId(com.vetclinic.app.R.id.working_hours)).check(matches(withSubstring("M-F 9:00 - 18:00")))
            onView(withId(com.vetclinic.app.R.id.pet_list)).check(RecyclerViewItemCountAssertion(1))
        }
    }

    @Test
    fun shouldOpenPetFragmentOnClickListItem() {

        val expectedPetName = "Pet Name"

        updateDiConfig {
            copy(
                mockedPetList = listOf(
                    PetDomain(
                        imageUrl = "image",
                        title = expectedPetName,
                        contentUrl = "https://google.com"
                    )
                )
            )
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use { scenario ->
            onView(withId(com.vetclinic.app.R.id.pet_list)).check(RecyclerViewItemCountAssertion(1))
            onView(withId(com.vetclinic.app.R.id.pet_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )

            //check pet fragment opened
            scenario.onActivity { activity ->
                Assert.assertTrue(
                    activity.supportFragmentManager.findFragmentByTag(PetFragment.TAG) != null
                )
            }

            // check pet title
            onView(
                allOf(
                    instanceOf(TextView::class.java),
                    withParent(withResourceName("action_bar"))
                )
            ).check(matches(withText(expectedPetName)))
        }
    }

    @Test
    fun shouldShowAlertOnCallClicked() {

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.call_btn)).check(matches(isDisplayed()))
            onView(withId(com.vetclinic.app.R.id.call_btn)).perform(click())

            // check alert
            onView(withText(com.vetclinic.app.R.string.alert_title))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldShowAlertOnChatClicked() {

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.chat_btn)).check(matches(isDisplayed()))
            onView(withId(com.vetclinic.app.R.id.chat_btn)).perform(click())

            // check alert
            onView(withText(com.vetclinic.app.R.string.alert_title))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldShowWorkingAlertOnChatClickedWithWorkingHours() {

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
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
    }

    @Test
    fun shouldShowNoWorkingAlertOnChatClickedWithNoWorkingHours() {

        updateDiConfig {
            copy(isWorkingHours = false)
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
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
    }

    @Test
    fun shouldShowWorkingAlertOnCallClickedWithWorkingHours() {

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
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
    }

    @Test
    fun shouldShowNoWorkingAlertOnCallClickedWithNoWorkingHours() {

        updateDiConfig {
            copy(isWorkingHours = false)
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
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

    @Test
    fun shouldCallButtonNotDisplayed() {

        updateDiConfig {
            copy(mockedConfig = mockedConfig.copy(
                isCallEnabled = false
            ))
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.call_btn)).check(matches(not(isDisplayed())))
            onView(withId(com.vetclinic.app.R.id.chat_btn)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldChatButtonNotDisplayed() {

        updateDiConfig {
            copy(mockedConfig = mockedConfig.copy(
                isChatEnabled = false
            ))
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.chat_btn)).check(matches(not(isDisplayed())))
            onView(withId(com.vetclinic.app.R.id.call_btn)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldChatAndCallButtonNotDisplayed() {

        updateDiConfig {
            copy(mockedConfig = mockedConfig.copy(
                isChatEnabled = false,
                isCallEnabled = false
            ))
        }

        waitForResourcesLoaded(2)

        launchActivity<MainActivity>().use {
            onView(withId(com.vetclinic.app.R.id.chat_btn)).check(matches(not(isDisplayed())))
            onView(withId(com.vetclinic.app.R.id.call_btn)).check(matches(not(isDisplayed())))
        }
    }
}