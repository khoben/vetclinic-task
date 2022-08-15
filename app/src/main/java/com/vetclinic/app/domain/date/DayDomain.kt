package com.vetclinic.app.domain.date

import androidx.annotation.IntDef


@Retention(AnnotationRetention.SOURCE)
@IntDef(
    DayDomain.SU,
    DayDomain.M,
    DayDomain.TU,
    DayDomain.W,
    DayDomain.TH,
    DayDomain.F,
    DayDomain.SA
)
annotation class DayDomain {
    companion object {
        const val SU = 1
        const val M = 2
        const val TU = 3
        const val W = 4
        const val TH = 5
        const val F = 6
        const val SA = 7
    }
}