package com.vetclinic.app.testing.mock

import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.date.*

data class DiContainerConfig(
    val mockedConfig: ConfigDomain = ConfigDomain(
        isChatEnabled = true,
        isCallEnabled = true,
        workingHours = WorkHoursDomain(
            "M-F 9:00 - 18:00",
            DateRangeDomain(
                DayDomain.M, DayDomain.F,
                TimeDomain(9, 0),
                TimeDomain(18, 0)
            )
        )
    ),
    val mockedPetList: List<PetDomain> = listOf(
        PetDomain(
            imageUrl = "image",
            title = "pet",
            contentUrl = "https://google.com"
        )
    ),
    val isConfigError: Boolean = false,
    val isPetListError: Boolean = false,
    val isWorkingHours: Boolean = true
)
