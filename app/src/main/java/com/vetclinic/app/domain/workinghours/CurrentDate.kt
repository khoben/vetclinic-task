package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.date.DateDomain
import com.vetclinic.app.domain.date.TimeDomain
import com.vetclinic.app.domain.workinghours.exceptions.InvalidCalendarProvidedException
import java.util.*

interface CurrentDate {
    fun get(): DateDomain

    class Base(private val calendar: Calendar = Calendar.getInstance(Locale.US)) : CurrentDate {

        init {
            if (calendar.firstDayOfWeek != Calendar.SUNDAY) {
                throw InvalidCalendarProvidedException()
            }
        }

        override fun get(): DateDomain {
            val d = calendar.get(Calendar.DAY_OF_WEEK)
            val h = calendar.get(Calendar.HOUR_OF_DAY)
            val m = calendar.get(Calendar.MINUTE)
            return DateDomain(d, TimeDomain(h, m))
        }
    }
}