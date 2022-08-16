package com.vetclinic.app.domain

import com.vetclinic.app.common.network.HttpService
import com.vetclinic.app.common.ui.Mapper
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.data.cloud.ConfigCloud

class FetchConfigUseCase(
    private val configService: HttpService<ConfigCloud>,
    private val mapper: Mapper<ConfigCloud, ConfigDomain>
) : UseCase<ConfigDomain> {

    override fun invoke(onResult: (ConfigDomain) -> Unit, onError: (Throwable) -> Unit) {
        try {
            configService.get({ onResult.invoke(mapper.map(it)) }, onError)
        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}