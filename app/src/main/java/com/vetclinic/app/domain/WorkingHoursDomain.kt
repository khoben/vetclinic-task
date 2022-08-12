package com.vetclinic.app.domain

data class WorkingHoursDomain(
    val origin: String,
    val from: HourDomain,
    val to: HourDomain
) {
    companion object {
        val EMPTY = WorkingHoursDomain(
            "",
            HourDomain.EMPTY,
            HourDomain.EMPTY
        )
    }
}