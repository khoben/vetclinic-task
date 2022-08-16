package com.vetclinic.app.data.cloud

import com.vetclinic.app.common.network.exception.BadHttpServiceCall
import com.vetclinic.app.data.cloud.exceptions.InvalidPetListDataException
import com.vetclinic.app.testing.mockOkHttpClient
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class PetHttpServiceTest {
    private val petsUri = "https://test/pets"

    @Test
    fun testConfigServiceSuccess() {
        val petsHttpService = PetHttpService(
            url = petsUri,
            unmarshallResponse = UnmarshallPetListResponse(),
            client = mockOkHttpClient(
                petsUri,
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
        val expectedCloudModel = listOf(
            PetCloud(
                "https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
                "Cat",
                "https://en.wikipedia.org/wiki/Cat",
                "2018-06-02T03:27:38.027Z"
            )
        )
        var actualCloudModel: List<PetCloud>? = null

        val countDownLatch = CountDownLatch(1)
        petsHttpService.get({
            actualCloudModel = it
            countDownLatch.countDown()
        }, {
            throw it
        })
        countDownLatch.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertEquals(expectedCloudModel, actualCloudModel)
    }

    @Test
    fun testConfigServiceIncorrectAnswer() {
        val petHttpService = PetHttpService(
            url = petsUri,
            unmarshallResponse = UnmarshallPetListResponse(),
            client = mockOkHttpClient(
                petsUri,
                "Bad"
            )
        )
        val countDownLatch = CountDownLatch(1)
        var actualException: Throwable? = RuntimeException()
        petHttpService.get({
            countDownLatch.countDown()
        }, {
            actualException = it
            countDownLatch.countDown()
        })
        countDownLatch.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(actualException is InvalidPetListDataException)
    }

    @Test
    fun testConfigServiceBadAnswer404() {
        internalTestConfigServiceBadAnswer(404)
    }

    @Test
    fun testConfigServiceBadAnswer500() {
        internalTestConfigServiceBadAnswer(500)
    }

    private fun internalTestConfigServiceBadAnswer(code: Int) {
        val petHttpService = PetHttpService(
            url = petsUri,
            unmarshallResponse = UnmarshallPetListResponse(),
            client = mockOkHttpClient(petsUri, code)
        )

        val countDownLatch = CountDownLatch(1)
        var occurredException: Throwable = RuntimeException()
        petHttpService.get({
            countDownLatch.countDown()
        }, {
            occurredException = it
            countDownLatch.countDown()
        })
        countDownLatch.await(2000, TimeUnit.MILLISECONDS)

        val actualException = occurredException
        Assert.assertTrue(actualException is BadHttpServiceCall && actualException.code == code)
    }
}