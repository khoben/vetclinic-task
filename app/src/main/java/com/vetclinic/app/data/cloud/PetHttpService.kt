package com.vetclinic.app.data.cloud

import com.vetclinic.app.common.network.HttpService
import com.vetclinic.app.common.network.UnmarshallResponse
import okhttp3.OkHttpClient


class PetHttpService(
    private val url: String,
    private val marshallPetListResponse: UnmarshallResponse<List<PetCloud>>,
    client: OkHttpClient
) : HttpService<List<PetCloud>>(client) {

    override fun get(
        onResult: (List<PetCloud>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        makeRequest(url, { response ->
            onResult.invoke(marshallPetListResponse.unmarshall(response))
        }, onError)
    }
}