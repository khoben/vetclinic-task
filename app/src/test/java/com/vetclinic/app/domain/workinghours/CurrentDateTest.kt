package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.date.DateDomain
import com.vetclinic.app.domain.date.TimeDomain
import com.vetclinic.app.domain.workinghours.exceptions.InvalidCalendarProvidedException
import com.vetclinic.app.testing.mockCalendar
import org.junit.Assert
import org.junit.Test
import java.util.*

class CurrentDateTest {

    private val expectedCurrentHour = DateDomain(2, TimeDomain(12, 15))

    @Test
    fun `test getting current hour with valid calendar`() {
        val calendar = mockCalendar(
            expectedCurrentHour.day,
            expectedCurrentHour.time.hour,
            expectedCurrentHour.time.minute
        )

        val currentDate = CurrentDate.Base(calendar)
        val actual = currentDate.get()

        Assert.assertEquals(actual, expectedCurrentHour)
    }

    @Test(expected = InvalidCalendarProvidedException::class)
    fun `should throw IllegalArgumentException if calendar's firstDayOfWeek is not Sunday`() {
        val calendar = mockCalendar(
            expectedCurrentHour.day,
            expectedCurrentHour.time.hour,
            expectedCurrentHour.time.minute,
            Locale.FRANCE
        )

        val currentTime = CurrentDate.Base(calendar)
    }
}