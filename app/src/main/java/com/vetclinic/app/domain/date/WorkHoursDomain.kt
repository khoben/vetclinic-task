package com.vetclinic.app.domain.date

data class WorkHoursDomain(
    val origin: String,
    val dateRange: DateRangeDomain
) {
    companion object {
        val EMPTY = WorkHoursDomain(
            "",
            DateRangeDomain.EMPTY
        )
    }
}