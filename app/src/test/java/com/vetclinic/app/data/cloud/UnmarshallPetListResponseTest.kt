package com.vetclinic.app.data.cloud

import com.vetclinic.app.Utils
import org.json.JSONException
import org.junit.Assert
import org.junit.Test

class UnmarshallPetListResponseTest {
    private val unmarshallPetResponse = UnmarshallPetListResponse()
    private val expected = listOf(
        PetCloud(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
            "Cat",
            "https://en.wikipedia.org/wiki/Cat",
            "2018-06-02T03:27:38.027Z"
        )
    )

    @Test
    fun testUnMarshall() {
        val actual = unmarshallPetResponse.unmarshall(
            Utils.genResponse(
                "{\n" +
                        "\t\"pets\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"image_url\": \"https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg\",\n" +
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
    fun testErrorUnMarshall() {
        unmarshallPetResponse.unmarshall(
            Utils.genResponse(
                "{\n" +
                        "\t\"pets\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"image_url\": \"https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg\",\n" +
                        "\t\t\t\"title\": \"Cat\",\n" +
                        "\t\t\t\"content_url\": \"https://en.wikipedia.org/wiki/Cat\",\n" +
                        "\t\t\t\"error\": \"2018-06-02T03:27:38.027Z\"\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}"
            )
        )
    }
}