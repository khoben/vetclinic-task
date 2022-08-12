package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.HourDomain
import java.util.*

interface CurrentHour {
    fun get(): HourDomain

    class Base(private val calendar: Calendar = Calendar.getInstance(Locale.US)) : CurrentHour {
        override fun get(): HourDomain {
            require(calendar.firstDayOfWeek == Calendar.SUNDAY)

            val d = calendar.get(Calendar.DAY_OF_WEEK)
            val h = calendar.get(Calendar.HOUR_OF_DAY)
            val m = calendar.get(Calendar.MINUTE)
            return HourDomain(d, h, m)
        }
    }
}