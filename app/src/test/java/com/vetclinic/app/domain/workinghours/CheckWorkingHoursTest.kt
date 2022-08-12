package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.HourDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import org.junit.Assert
import org.junit.Test

class CheckWorkingHoursTest {

    private val workingHours = WorkingHoursDomain(
        "M-F 9:00 - 18:00",
        HourDomain(2, 9, 0),
        HourDomain(6, 18, 0)
    )

    private val currentHourNotWorking = object : CurrentHour {
        override fun get(): HourDomain {
            return HourDomain(2, 0, 0)
        }
    }

    private val currentHourWorking = object : CurrentHour {
        override fun get(): HourDomain {
            return HourDomain(2, 15, 0)
        }
    }

    private val checkWorkingHoursWorking = CheckWorkingHours.Base(currentHourWorking)
    private val checkWorkingHoursNotWorking = CheckWorkingHours.Base(currentHourNotWorking)

    @Test
    fun testWorking() {
        Assert.assertTrue(checkWorkingHoursWorking.check(workingHours))
    }

    @Test
    fun testNotWorking() {
        Assert.assertFalse(checkWorkingHoursNotWorking.check(workingHours))
    }
}