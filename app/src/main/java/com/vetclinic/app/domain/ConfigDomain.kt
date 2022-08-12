package com.vetclinic.app.domain

data class ConfigDomain(
    val isChatEnabled: Boolean,
    val isCallEnabled: Boolean,
    val workingHours: WorkingHoursDomain
) {
    companion object {
        val EMPTY = ConfigDomain(
            isChatEnabled = false,
            isCallEnabled = false,
            workingHours = WorkingHoursDomain.EMPTY
        )
    }
}