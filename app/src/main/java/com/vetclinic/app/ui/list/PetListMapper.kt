package com.vetclinic.app.ui.list

import com.vetclinic.app.common.ui.Mapper
import com.vetclinic.app.data.cloud.PetCloud
import com.vetclinic.app.domain.PetDomain

class PetListMapper : Mapper<List<PetCloud>, List<PetDomain>> {
    override fun map(data: List<PetCloud>): List<PetDomain> {
        return data.map { PetDomain(it.imageUrl, it.title, it.contentUrl) }
    }
}