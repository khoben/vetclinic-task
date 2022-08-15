package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.date.WorkHoursDomain

interface CheckWorkHours {
    fun check(workHours: WorkHoursDomain): Boolean

    class Base(private val currentDate: CurrentDate) : CheckWorkHours {
        override fun check(workHours: WorkHoursDomain): Boolean {
            val currentDate = currentDate.get()
            return workHours.dateRange.isInRange(currentDate)
        }
    }
}