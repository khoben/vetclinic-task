package com.vetclinic.app

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

object Utils {
    fun genResponse(jsonString: String): Response {
        return Response.Builder()
            .request(Request.Builder().url("https://google.com").build())
            .code(200)
            .message("")
            .protocol(Protocol.HTTP_1_1)
            .body(
                ResponseBody.create(
                    "application/json".toMediaTypeOrNull(),
                    jsonString
                )
            ).build()
    }
}