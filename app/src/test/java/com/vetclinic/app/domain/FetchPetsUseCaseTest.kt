package com.vetclinic.app.domain

import com.vetclinic.app.data.cloud.PetHttpService
import com.vetclinic.app.data.cloud.UnmarshallPetListResponse
import com.vetclinic.app.data.cloud.exceptions.InvalidPetListDataException
import com.vetclinic.app.testing.mockOkHttpClient
import com.vetclinic.app.ui.list.PetListMapper
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FetchPetsUseCaseTest {
    private val mockUrl = "https://test/pets"

    @Test
    fun testPetsUseCase() {

        val useCase = FetchPetsUseCase(
            PetHttpService(
                url = mockUrl,
                unmarshallResponse = UnmarshallPetListResponse(),
                client = mockOkHttpClient(
                    mockUrl,
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
            ),
            PetListMapper()
        )

        val expected = listOf(
            PetDomain(
                "https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
                "Cat",
                "https://en.wikipedia.org/wiki/Cat"
            )
        )
        var data: List<PetDomain> = emptyList()

        val lock = CountDownLatch(1)
        useCase.invoke({
            data = it
            lock.countDown()
        }, {
            Assert.fail()
        })

        lock.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertEquals(expected, data)
    }

    @Test
    fun testFailedPetsUseCase() {

        val useCase = FetchPetsUseCase(
            PetHttpService(
                url = mockUrl,
                unmarshallResponse = UnmarshallPetListResponse(),
                client = mockOkHttpClient(
                    mockUrl,
                    "failed"
                )
            ),
            PetListMapper()
        )

        var error: Throwable = RuntimeException()
        val lock = CountDownLatch(1)
        useCase.invoke({
            Assert.fail()
        }, {
            error = it
        })

        lock.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(error is InvalidPetListDataException)
    }
}