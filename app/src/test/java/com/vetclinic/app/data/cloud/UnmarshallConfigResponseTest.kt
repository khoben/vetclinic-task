package com.vetclinic.app.data.cloud

import com.vetclinic.app.data.cloud.exceptions.InvalidConfigDataException
import com.vetclinic.app.testing.mockJsonResponse
import org.json.JSONException
import org.junit.Assert
import org.junit.Test

class UnmarshallConfigResponseTest {
    private val unmarshallConfigResponse = UnmarshallConfigResponse()

    @Test
    fun testSuccessResponseUnmarshall() {
        val expected = ConfigCloud(
            isChatEnabled = true,
            isCallEnabled = true,
            workHours = "M-F 9:00 - 18:00"
        )
        val actual = unmarshallConfigResponse.unmarshall(
            mockJsonResponse(
                "{\n" +
                        "\t\"settings\": {\n" +
                        "\t\t\"isChatEnabled\": true,\n" +
                        "\t\t\"isCallEnabled\": true,\n" +
                        "\t\t\"workHours\": \"M-F 9:00 - 18:00\"\n" +
                        "\t}\n" +
                        "}"
            )
        )
        Assert.assertEquals(expected, actual)
    }

    @Test(expected = InvalidConfigDataException::class)
    fun testFailResponseUnmarshall() {
        unmarshallConfigResponse.unmarshall(
            mockJsonResponse(
                "{\n" +
                        "\t\"settings\": {\n" +
                        "\t\t\"isChatEnabled\": true,\n" +
                        "\t\t\"isCallEnabled\": true\n" +
                        "\t}\n" +
                        "}"
            )
        )
    }

    @Test(expected = InvalidConfigDataException::class)
    fun testEmptyResponseUnmarshall() {
        unmarshallConfigResponse.unmarshall(
            mockJsonResponse("")
        )
    }
}