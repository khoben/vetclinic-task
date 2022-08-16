package com.vetclinic.app.common.fetchimage.cache

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class KeyHashTest(private val key: String, private val expectedHash: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters(): Iterable<Array<Any>> {
            return listOf(
                arrayOf(
                    "teststring",
                    "d67c5cbf5b01c9f91932e3b8def5e5f8"
                ),
                arrayOf(
                    "",
                    "d41d8cd98f00b204e9800998ecf8427e"
                ),
                arrayOf(
                    "path/to/path.jpg",
                    "44c68459451ef8d43fb5455b912f9f81"
                ),
                arrayOf(
                    "***",
                    "8a7ab20ec0ab3262ce329c7dcb399a4e"
                ),
                arrayOf(
                    " ",
                    "7215ee9c7d9dc229d2921a40e899ec5f"
                ),
                arrayOf(
                    "kfmapsfkpsadofkpoadskfpaosdfkpasdofkapsodfkaspdofkaspdfokasdf" +
                            "fasdfsaodfiasdfasdfsadkfasdfkaspdfkoasdpofkasdfkaspodfkaso" +
                            "fodjfiasdjfsdiaojfaoisfjiosadjfoiadsfjaoisdfjaiosdfjiasodfj" +
                            "fsdafasidfjasidfjasdpfjasdfjasdfpjasdfpajsdfpiasdjfapisfjaps" +
                            "faspdjfipsadfjasdifjrijr  ijfsapifjaf",
                    "5d2d6826af1f64918307aac3d34df288"
                )
            )
        }
    }

    private val keyHash = KeyHash.MD5()
    private val expectedLength = 32

    @Test
    fun testKeyHashMD5() {
        val hash = keyHash.hash(key)
        Assert.assertEquals(expectedHash, hash)
        Assert.assertEquals(expectedLength, hash.length)
    }
}