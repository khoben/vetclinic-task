package com.vetclinic.app.data.cloud

import com.vetclinic.app.testing.mockJsonResponse
import org.json.JSONException
import org.junit.Assert
import org.junit.Test

class UnmarshallPetListResponseTest {
    private val unmarshallPetResponse = UnmarshallPetListResponse()

    @Test
    fun testSuccessResponseUnmarshall() {
        val expected = listOf(
            PetCloud(
                "https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
                "Cat",
                "https://en.wikipedia.org/wiki/Cat",
                "2018-06-02T03:27:38.027Z"
            )
        )

        val actual = unmarshallPetResponse.unmarshall(
            mockJsonResponse(
                "{\n" +
                        "\t\"pets\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"image_url\": \"https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg\",\n" +
                        "\t\t\t\"title\": \"Cat\",\n" +
                        "\t\t\t\"content_url\": \"https://en.wikipedia.org/wiki/Cat\",\n" +
                        "\t\t\t\"date_added\": \"2018-06-02T03:27:38.027Z\"\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}"
            )
        )
        Assert.assertEquals(expected, actual)
    }

    @Test(expected = JSONException::class)
    fun testFailResponseUnmarshall() {
        unmarshallPetResponse.unmarshall(
            mockJsonResponse(
                "{\n" +
                        "\t\"pets\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"image_url\": \"https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg\",\n" +
                        "\t\t\t\"title\": \"Cat\",\n" +
                        "\t\t\t\"content_url\": \"https://en.wikipedia.org/wiki/Cat\",\n" +
                        "\t\t\t\"error\": \"2018-06-02T03:27:38.027Z\"\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}"
            )
        )
    }

    @Test(expected = JSONException::class)
    fun testEmptyResponseUnmarshall() {
        unmarshallPetResponse.unmarshall(
            mockJsonResponse("")
        )
    }
}