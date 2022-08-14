package com.vetclinic.app.testing

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mock.*

fun mockJsonResponse(jsonString: String) = Response.Builder()
    .request(Request.Builder().url("https://mocked.com").build())
    .code(200)
    .message("Mocked response")
    .protocol(Protocol.HTTP_1_1)
    .body(jsonString.toResponseBody("application/json".toMediaTypeOrNull()))
    .build()

fun mockOkHttpClient(uri: String, body: String) = OkHttpClient().newBuilder().addInterceptor(
        MockInterceptor().apply {
            rule(get, url eq uri) { respond(body) }
        }
    ).build()

fun mockOkHttpClient(uri: String, code: Int) = OkHttpClient().newBuilder().addInterceptor(
    MockInterceptor().apply {
        rule(get, url eq uri) { respond(code) }
    }
).build()