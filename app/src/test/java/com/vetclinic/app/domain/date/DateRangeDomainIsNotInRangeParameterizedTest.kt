package com.vetclinic.app.domain.date

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DateRangeDomainIsNotInRangeParameterizedTest(
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
                DateDomain(DayDomain.M, TimeDomain(8, 59)),
                DateDomain(DayDomain.TU, TimeDomain(18, 1)),
                DateDomain(DayDomain.SU, TimeDomain(9, 0)),
                DateDomain(DayDomain.SA, TimeDomain(10, 0)),
                DateDomain(DayDomain.F, TimeDomain(0, 0)),
            )
        }
    }

    @Test
    fun testIsNotInRange() {
        Assert.assertFalse(dateRangeDomain.isInRange(dateDomain))
    }
}