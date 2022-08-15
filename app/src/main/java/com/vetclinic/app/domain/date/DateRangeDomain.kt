package com.vetclinic.app.domain.date

import com.vetclinic.app.domain.workinghours.exceptions.DateRangeInvalidFormatException

data class DateRangeDomain(
    @DayDomain val dayFrom: Int,
    @DayDomain val dayTo: Int,
    val hourFrom: TimeDomain,
    val hourTo: TimeDomain
) {
    init {
        if (dayFrom > dayTo || hourFrom > hourTo) {
            throw DateRangeInvalidFormatException()
        }
    }

    fun isInRange(date: DateDomain): Boolean {
        return date.day in dayFrom..dayTo
                && date.time >= hourFrom && date.time <= hourTo
    }

    companion object {
        val EMPTY = DateRangeDomain(
            DayDomain.M,
            DayDomain.M,
            TimeDomain.EMPTY,
            TimeDomain.EMPTY
        )
    }
}
