package com.vetclinic.app.domain

data class HourDomain(
    val day: Int,
    val hour: Int,
    val minute: Int
) : Comparable<HourDomain> {
    override fun compareTo(other: HourDomain): Int {
        if (this.day > other.day) return 1
        if (this.day < other.day) return -1
        if (this.hour > other.hour) return 1
        if (this.hour < other.hour) return -1
        if (this.minute > other.minute) return 1
        if (this.minute < other.minute) return -1
        return 0
    }

    companion object {
        val EMPTY = HourDomain(-1, -1, -1)
    }
}