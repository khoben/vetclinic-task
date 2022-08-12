package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.HourDomain
import org.junit.Assert
import org.junit.Test
import java.util.*

class CurrentHourTest {

    private val expectedHour = HourDomain(Calendar.MONDAY, 12, 15)

    private val currentHour = CurrentHour.Base(object : Calendar(TimeZone.getDefault(), Locale.US) {
        override fun computeTime() = Unit
        override fun computeFields() = Unit
        override fun add(field: Int, amount: Int) = Unit
        override fun roll(field: Int, up: Boolean) = Unit
        override fun getMinimum(field: Int): Int = 0
        override fun getMaximum(field: Int): Int = 0
        override fun getGreatestMinimum(field: Int): Int = 0
        override fun getLeastMaximum(field: Int): Int = 0

        override fun get(field: Int): Int {
            return when (field) {
                DAY_OF_WEEK -> expectedHour.day
                HOUR_OF_DAY -> expectedHour.hour
                MINUTE -> expectedHour.minute
                else -> 0
            }
        }
    })

    @Test
    fun testCurrentHour() {
        val actual = currentHour.get()
        Assert.assertEquals(actual, expectedHour)
    }
}