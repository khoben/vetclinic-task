package com.vetclinic.app.ui.list

import com.vetclinic.app.data.cloud.ConfigCloud
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.date.*
import com.vetclinic.app.domain.workinghours.ParseWorkingHours
import org.junit.Assert
import org.junit.Test

class ConfigMapperTest {

    private val configMapper = ConfigMapper(ParseWorkingHours.Base())

    @Test
    fun testConfigMapper() {
        val data = ConfigCloud(
            isChatEnabled = true,
            isCallEnabled = true,
            workHours = "M-F 9:00 - 18:00"
        )

        val expected = ConfigDomain(
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
        )

        val actual = configMapper.map(data)

        Assert.assertEquals(expected, actual)
    }
}