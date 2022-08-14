package com.vetclinic.app.data.cloud

import com.vetclinic.app.common.network.HttpService
import com.vetclinic.app.common.network.UnmarshallResponse
import okhttp3.OkHttpClient


class ConfigHttpService(
    private val url: String,
    private val unmarshallResponse: UnmarshallResponse<ConfigCloud>,
    client: OkHttpClient
) : HttpService<ConfigCloud>(client) {

    override fun get(
        onResult: (ConfigCloud) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        makeRequest(url, { response ->
            try {
                onResult.invoke(unmarshallResponse.unmarshall(response))
            } catch (e: Exception) {
                onError.invoke(e)
            }
        }, onError)
    }
}