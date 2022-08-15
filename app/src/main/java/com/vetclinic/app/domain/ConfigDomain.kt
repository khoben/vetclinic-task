package com.vetclinic.app.domain

import com.vetclinic.app.domain.date.WorkHoursDomain

data class ConfigDomain(
    val isChatEnabled: Boolean,
    val isCallEnabled: Boolean,
    val workingHours: WorkHoursDomain
) {
    companion object {
        val EMPTY = ConfigDomain(
            isChatEnabled = false,
            isCallEnabled = false,
            workingHours = WorkHoursDomain.EMPTY
        )
    }
}