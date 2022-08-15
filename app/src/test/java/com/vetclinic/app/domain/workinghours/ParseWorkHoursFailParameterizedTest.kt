package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.workinghours.exceptions.WorkHoursInvalidParseException
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParseWorkHoursFailParameterizedTest(
    private val workingHoursString: String
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters(): Iterable<String> {
            return listOf(
                "",
                "F-M 17:00 - 18:00",
                "M-F 23:00 - 15:00",
                "Invalid"
            )
        }
    }

    private val parseWorkingHours = ParseWorkingHours.Base()

    @Test(expected = WorkHoursInvalidParseException::class)
    fun testIncorrectParse() {
        parseWorkingHours.parse(workingHoursString)
    }
}