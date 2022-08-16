package com.vetclinic.app.common.fetchimage.cache

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import com.vetclinic.app.common.fetchimage.decode.ImageDecoder
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PersistenceCacheTest {

    private lateinit var persistenceCache: ImageCache.FileImageCache

    @Before
    fun before() {
        val context: Context = ApplicationProvider.getApplicationContext()
        persistenceCache = ImageCache.FileImageCache(
            context,
            KeyHash.MD5(),
            ImageDecoder.FileDecoder(),
            "testcache"
        )
    }

    @After
    fun after() {
        persistenceCache.clear()
    }

    @Test
    fun testGetImageThatNotExists() {
        Assert.assertTrue(persistenceCache.get("some-key") == null)
    }

    @Test
    fun testGetImageThatExists() {
        val image = Bitmap.createBitmap(123, 123, Bitmap.Config.ARGB_8888)
        persistenceCache.store("some-key", image)
        val actualImage = persistenceCache.get("some-key")
        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == 123 && actualImage.height == 123)
    }

    @Test
    fun testClearCache() {
        val image = Bitmap.createBitmap(123, 123, Bitmap.Config.ARGB_8888)
        persistenceCache.store("some-key", image)
        val actualImage = persistenceCache.get("some-key")

        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == 123 && actualImage.height == 123)

        persistenceCache.clear()
        Assert.assertTrue(persistenceCache.get("some-key") == null)
    }

    @Test
    fun testStoreAfterClearCache() {
        val image = Bitmap.createBitmap(123, 123, Bitmap.Config.ARGB_8888)
        persistenceCache.store("some-key", image)
        val actualImage = persistenceCache.get("some-key")

        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == 123 && actualImage.height == 123)

        persistenceCache.clear()
        Assert.assertTrue(persistenceCache.get("some-key") == null)

        val image2 = Bitmap.createBitmap(321, 321, Bitmap.Config.ARGB_8888)
        persistenceCache.store("some-key", image2)
        val actualImage2 = persistenceCache.get("some-key")

        Assert.assertTrue(actualImage2 != null)
        Assert.assertTrue(actualImage2!!.width == 321 && actualImage2.height == 321)
    }

    @Test
    fun testStoreSameKey() {
        val image = Bitmap.createBitmap(123, 123, Bitmap.Config.ARGB_8888)
        val image2 = Bitmap.createBitmap(321, 321, Bitmap.Config.ARGB_8888)
        persistenceCache.store("some-key", image)
        val actualImage = persistenceCache.get("some-key")

        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == 123 && actualImage.height == 123)

        persistenceCache.store("some-key", image2)

        val actualImage2 = persistenceCache.get("some-key")
        Assert.assertTrue(actualImage2 != null)
        Assert.assertTrue(actualImage2!!.width == 321 && actualImage2.height == 321)
    }

}