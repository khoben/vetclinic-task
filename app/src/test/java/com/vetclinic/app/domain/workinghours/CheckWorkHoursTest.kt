package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.date.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CheckWorkHoursTest(
    private val checkWorkHours: CheckWorkHours,
    private val expectedIsWorking: Boolean
) {

    private val workingHours = WorkHoursDomain(
        "M-F 9:00 - 18:00",
        DateRangeDomain(
            DayDomain.M,
            DayDomain.F,
            TimeDomain(9, 0),
            TimeDomain(18, 0)
        )
    )

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters(): Iterable<Array<Any>> {
            return listOf(
                arrayOf(
                    CheckWorkHours.Base(object : CurrentDate {
                        override fun get(): DateDomain {
                            return DateDomain(DayDomain.M, TimeDomain(15, 0))
                        }
                    }),
                    true
                ), arrayOf(
                    CheckWorkHours.Base(object : CurrentDate {
                        override fun get(): DateDomain {
                            return DateDomain(DayDomain.M, TimeDomain(0, 0))
                        }
                    }),
                    false
                ), arrayOf(
                    CheckWorkHours.Base(object : CurrentDate {
                        override fun get(): DateDomain {
                            return DateDomain(DayDomain.TH, TimeDomain(8, 59))
                        }
                    }),
                    false
                ), arrayOf(
                    CheckWorkHours.Base(object : CurrentDate {
                        override fun get(): DateDomain {
                            return DateDomain(DayDomain.F, TimeDomain(18, 0))
                        }
                    }),
                    true
                )
            )
        }
    }

    @Test
    fun testWorkingHours() {
        Assert.assertTrue(checkWorkHours.check(workingHours) == expectedIsWorking)
    }
}