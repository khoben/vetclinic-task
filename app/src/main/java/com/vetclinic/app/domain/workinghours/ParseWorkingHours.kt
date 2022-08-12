package com.vetclinic.app.domain.workinghours

import com.vetclinic.app.domain.HourDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import com.vetclinic.app.domain.workinghours.exceptions.InvalidParseWorkingHours

interface ParseWorkingHours {
    fun parse(s: String): WorkingHoursDomain

    class Base(private val weekDays: Map<String, Int> = LOCALE_US_WEEK) : ParseWorkingHours {
        override fun parse(s: String): WorkingHoursDomain {
            try {
                val (dayRange, fromHours, _, toHours) = s.split(' ')
                val (fromHour, fromMinute) = fromHours.split(':')
                val (toHour, toMinute) = toHours.split(':')
                val (fromDay, toDay) = dayRange.split('-')

                return WorkingHoursDomain(
                    origin = s,
                    from = HourDomain(
                        weekDays[fromDay] ?: -1,
                        fromHour.toInt(),
                        fromMinute.toInt()
                    ),
                    to = HourDomain(
                        weekDays[toDay] ?: -1,
                        toHour.toInt(),
                        toMinute.toInt()
                    )
                )
            } catch (e: Exception) {
                throw InvalidParseWorkingHours(e)
            }
        }

        companion object {
            private val LOCALE_US_WEEK = mapOf(
                "SU" to 1,
                "M" to 2,
                "TU" to 3,
                "W" to 4,
                "TH" to 5,
                "F" to 6,
                "SA" to 7
            )
        }
    }
}