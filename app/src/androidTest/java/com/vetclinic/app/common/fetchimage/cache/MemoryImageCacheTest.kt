package com.vetclinic.app.common.fetchimage.cache

import android.graphics.Bitmap
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MemoryImageCacheTest {

    private lateinit var memoryCache: ImageCache.MemoryImageCache

    @Before
    fun before() {
        memoryCache = ImageCache.MemoryImageCache()
    }

    @After
    fun after() {
        memoryCache.clear()
    }

    @Test
    fun testGetImageThatNotExists() {
        Assert.assertTrue(memoryCache.get("some-key") == null)
    }

    @Test
    fun testGetImageThatExists() {
        val image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        memoryCache.store("some-key", image)
        Assert.assertTrue(memoryCache.get("some-key") === image)
    }

    @Test
    fun testClearCache() {
        val image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        memoryCache.store("some-key", image)
        Assert.assertTrue(memoryCache.get("some-key") === image)
        memoryCache.clear()
        Assert.assertTrue(memoryCache.get("some-key") == null)
    }

    @Test
    fun testStoreSameKey() {
        val image1 = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val image2 = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        memoryCache.store("some-key", image1)
        Assert.assertTrue(memoryCache.get("some-key") === image1)

        memoryCache.store("some-key", image2)
        Assert.assertTrue(memoryCache.get("some-key") === image2)
    }
}