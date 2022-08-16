package com.vetclinic.app.domain

import com.vetclinic.app.common.network.HttpService
import com.vetclinic.app.common.ui.Mapper
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.data.cloud.PetCloud

class FetchPetsUseCase(
    private val petService: HttpService<List<PetCloud>>,
    private val mapper: Mapper<List<PetCloud>, List<PetDomain>>
) : UseCase<List<PetDomain>> {

    override fun invoke(onResult: (List<PetDomain>) -> Unit, onError: (Throwable) -> Unit) {
        try {
            petService.get({ onResult.invoke(mapper.map(it)) }, onError)
        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}