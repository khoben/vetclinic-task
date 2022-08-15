package com.vetclinic.app.domain.date

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DateRangeDomainIsInRangeParameterizedTest(
    private val dateDomain: DateDomain
) {

    private val dateRangeDomain = DateRangeDomain(
        DayDomain.M,
        DayDomain.F,
        TimeDomain(9, 0),
        TimeDomain(18, 0)
    )

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters(): Iterable<DateDomain> {
            return listOf(
                DateDomain(DayDomain.M, TimeDomain(9, 0)),
                DateDomain(DayDomain.TU, TimeDomain(10, 0)),
                DateDomain(DayDomain.W, TimeDomain(18, 0)),
                DateDomain(DayDomain.TH, TimeDomain(10, 0)),
                DateDomain(DayDomain.F, TimeDomain(10, 0)),
            )
        }
    }

    @Test
    fun testIsInRange() {
        Assert.assertTrue(dateRangeDomain.isInRange(dateDomain))
    }
}