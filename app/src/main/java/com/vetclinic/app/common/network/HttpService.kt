package com.vetclinic.app.common.network

import okhttp3.OkHttpClient

abstract class HttpService<R>(client: OkHttpClient) : MakeRequest(client) {
    abstract fun get(onResult: (R) -> Unit, onError: (Throwable) -> Unit)
}