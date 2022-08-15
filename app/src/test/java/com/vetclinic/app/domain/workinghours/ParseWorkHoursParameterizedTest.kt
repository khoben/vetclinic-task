package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.date.DateRangeDomain
import com.vetclinic.app.domain.date.DayDomain
import com.vetclinic.app.domain.date.TimeDomain
import com.vetclinic.app.domain.date.WorkHoursDomain
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParseWorkHoursParameterizedTest(
    private val workingHoursString: String,
    private val expectedWorkingHours: WorkHoursDomain
) {

    private val parseWorkingHours = ParseWorkingHours.Base()

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters(): Iterable<Array<Any>> {
            return listOf(
                arrayOf(
                    "M-F 9:00 - 18:00",
                    WorkHoursDomain(
                        "M-F 9:00 - 18:00",
                        DateRangeDomain(
                            DayDomain.M, DayDomain.F,
                            TimeDomain(9, 0),
                            TimeDomain(18, 0)
                        )
                    )
                ),
                arrayOf(
                    "W-SA 9:00 - 18:00",
                    WorkHoursDomain(
                        "W-SA 9:00 - 18:00",
                        DateRangeDomain(
                            DayDomain.W, DayDomain.SA,
                            TimeDomain(9, 0),
                            TimeDomain(18, 0)
                        )
                    )
                ),
                arrayOf(
                    "TH-SA 0:00 - 23:59",
                    WorkHoursDomain(
                        "TH-SA 0:00 - 23:59",
                        DateRangeDomain(
                            DayDomain.TH, DayDomain.SA,
                            TimeDomain(0, 0),
                            TimeDomain(23, 59)
                        )
                    )
                ),
                arrayOf(
                    "M-M 12:43 - 12:43",
                    WorkHoursDomain(
                        "M-M 12:43 - 12:43",
                        DateRangeDomain(
                            DayDomain.M, DayDomain.M,
                            TimeDomain(12, 43),
                            TimeDomain(12, 43)
                        )
                    )
                )
            )
        }
    }

    @Test
    fun testParseValidData() {
        Assert.assertEquals(expectedWorkingHours, parseWorkingHours.parse(workingHoursString))
    }
}