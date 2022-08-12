package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.HourDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import com.vetclinic.app.domain.workinghours.exceptions.InvalidParseWorkingHours
import org.junit.Assert
import org.junit.Test

class ParseWorkingHoursTest {
    private val parseWorkingHours = ParseWorkingHours.Base()
    private val workingHoursString = "M-F 9:00 - 18:00"
    private val workingHoursStringIncorrect = "MF 9:00 - 18:00"

    @Test
    fun testParse() {
        val actual = parseWorkingHours.parse(workingHoursString)
        val expected = WorkingHoursDomain(
            workingHoursString,
            HourDomain(2, 9, 0),
            HourDomain(6, 18, 0)
        )
        Assert.assertEquals(expected, actual)
    }

    @Test(expected = InvalidParseWorkingHours::class)
    fun testIncorrectParse() {
        parseWorkingHours.parse(workingHoursStringIncorrect)
    }
}