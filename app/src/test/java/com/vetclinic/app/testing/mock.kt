package com.vetclinic.app.testing

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mock.*
import java.util.*

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

fun mockCalendar(dayOfWeek: Int, hourOfDay: Int, minute: Int, locale: Locale = Locale.US) =
    object : Calendar(TimeZone.getDefault(), locale) {
        override fun computeTime() = Unit
        override fun computeFields() = Unit
        override fun add(field: Int, amount: Int) = Unit
        override fun roll(field: Int, up: Boolean) = Unit
        override fun getMinimum(field: Int): Int = 0
        override fun getMaximum(field: Int): Int = 0
        override fun getGreatestMinimum(field: Int): Int = 0
        override fun getLeastMaximum(field: Int): Int = 0

        override fun get(field: Int): Int {
            return when (field) {
                DAY_OF_WEEK -> dayOfWeek
                HOUR_OF_DAY -> hourOfDay
                MINUTE -> minute
                else -> 0
            }
        }
    }