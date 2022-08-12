package com.vetclinic.app.common.network

import com.vetclinic.app.common.network.exception.BadHttpServiceCall
import okhttp3.*
import java.io.IOException

abstract class HttpService<R>(private val client: OkHttpClient) {

    abstract fun get(
        onResult: (R) -> Unit,
        onError: (Throwable) -> Unit
    )

    protected fun makeRequest(
        url: String,
        onResult: (Response) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError.invoke(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    onResult.invoke(response)
                } else {
                    onError.invoke(BadHttpServiceCall())
                }
            }
        })
    }
}