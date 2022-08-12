package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.WorkingHoursDomain

interface CheckWorkingHours {
    fun check(workingHours: WorkingHoursDomain): Boolean

    class Base(private val currentHour: CurrentHour) : CheckWorkingHours {
        override fun check(workingHours: WorkingHoursDomain): Boolean {
            val current = currentHour.get()
            return current >= workingHours.from && current <= workingHours.to
        }
    }
}