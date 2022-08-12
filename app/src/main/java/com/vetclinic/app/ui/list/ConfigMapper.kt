package com.vetclinic.app.ui.list

import com.vetclinic.app.common.ui.Mapper
import com.vetclinic.app.data.cloud.ConfigCloud
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.workinghours.ParseWorkingHours

class ConfigMapper(
    private val parseWorkingHours: ParseWorkingHours
) : Mapper<ConfigCloud, ConfigDomain> {
    override fun map(data: ConfigCloud): ConfigDomain {
        return ConfigDomain(
            data.isChatEnabled,
            data.isCallEnabled,
            parseWorkingHours.parse(data.workHours)
        )
    }
}