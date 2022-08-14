package com.vetclinic.app.ui.list

import com.vetclinic.app.data.cloud.PetCloud
import com.vetclinic.app.domain.PetDomain
import org.junit.Assert
import org.junit.Test

class PetListMapperTest {
    private val petListMapper = PetListMapper()

    @Test
    fun testPetListMapper() {
        val data = listOf(
            PetCloud(
                "https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
                "Cat",
                "https://en.wikipedia.org/wiki/Cat",
                "2018-06-02T03:27:38.027Z"
            )
        )
        val expected = listOf(
            PetDomain(
                "https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
                "Cat",
                "https://en.wikipedia.org/wiki/Cat"
            )
        )

        val actual = petListMapper.map(data)

        Assert.assertEquals(expected, actual)
    }
}