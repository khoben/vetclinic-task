package com.vetclinic.app.data.cloud

import com.vetclinic.app.Utils.genResponse
import org.json.JSONException
import org.junit.Assert
import org.junit.Test

class UnmarshallConfigResponseTest {
    private val unmarshallConfigResponse = UnmarshallConfigResponse()
    private val expected = ConfigCloud(true, true, "M-F 9:00 - 18:00")

    @Test
    fun testUnMarshall() {
        val actual = unmarshallConfigResponse.unmarshall(
            genResponse(
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

    @Test(expected = JSONException::class)
    fun testErrorUnMarshall() {
        unmarshallConfigResponse.unmarshall(
            genResponse(
                "{\n" +
                        "\t\"settings\": {\n" +
                        "\t\t\"isChatEnabled\": true,\n" +
                        "\t\t\"isCallEnabled\": true\n" +
                        "\t}\n" +
                        "}"
            )
        )
    }
}