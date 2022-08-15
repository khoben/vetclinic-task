package com.vetclinic.app.domain.date

import androidx.annotation.IntRange
import com.vetclinic.app.domain.workinghours.exceptions.TimeInvalidFormatException

data class TimeDomain(
    @IntRange(from = 0, to = 23) val hour: Int,
    @IntRange(from = 0, to = 59) val minute: Int
) : Comparable<TimeDomain> {

    init {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw TimeInvalidFormatException()
        }
    }

    override fun compareTo(other: TimeDomain): Int {
        if (hour > other.hour) return 1
        if (hour < other.hour) return -1
        if (minute > other.minute) return 1
        if (minute < other.minute) return -1
        return 0
    }

    companion object {
        val EMPTY = TimeDomain(0, 0)
    }
}