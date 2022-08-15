package com.vetclinic.app.domain.date

data class DateDomain(
    @DayDomain val day: Int,
    val time: TimeDomain
)