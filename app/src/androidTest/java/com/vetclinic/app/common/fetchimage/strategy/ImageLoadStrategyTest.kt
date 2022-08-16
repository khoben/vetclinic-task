package com.vetclinic.app.common.fetchimage.strategy

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import com.vetclinic.app.common.fetchimage.cache.ImageCache
import com.vetclinic.app.common.fetchimage.cache.KeyHash
import com.vetclinic.app.common.fetchimage.decode.ImageDecoder
import com.vetclinic.app.common.fetchimage.decode.TempFile
import com.vetclinic.app.common.fetchimage.target.ComputeScale
import com.vetclinic.app.common.fetchimage.target.TargetSize
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ImageLoadStrategyTest {

    @Test
    fun testSingleMemory() {
        val cache = ImageCache.MemoryImageCache()
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.store("url", image)
        val strategy = ImageLoadStrategy.Memory(cache)
        var actualImage: Bitmap? = null
        strategy.proceed("url", TargetSize(100, 100), {
            actualImage = it
        })

        Assert.assertEquals(image, actualImage)
    }

    @Test
    fun testSinglePersistence() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val cache = ImageCache.FileImageCache(
            context,
            KeyHash.MD5(),
            ImageDecoder.FileDecoder(),
            "testcache"
        )
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.store("url", image)
        val strategy = ImageLoadStrategy.Persistence(cache, Executors.newSingleThreadExecutor())
        var actualImage: Bitmap? = null
        val lock = CountDownLatch(1)
        strategy.proceed("url", TargetSize(100, 100), {
            actualImage = it
            lock.countDown()
        })

        lock.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == image.width && actualImage!!.height == image.height)

        cache.clear()
    }

    @Test
    fun testSingleNetwork() {
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val strategy = createNetworkLoadStrategy(image)

        var actualImage: Bitmap? = null
        val lock = CountDownLatch(1)
        strategy.proceed("https://goodurl", TargetSize(100, 100), {
            actualImage = it
            lock.countDown()
        })

        lock.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == 100 && actualImage!!.height == 100)
    }

    @Test
    fun testMemoryPersistenceChain() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val cache = ImageCache.FileImageCache(
            context,
            KeyHash.MD5(),
            ImageDecoder.FileDecoder(),
            "testcache"
        )
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.store("url", image)
        val strategy2 = ImageLoadStrategy.Persistence(cache, Executors.newSingleThreadExecutor())

        val memoryCache = ImageCache.MemoryImageCache()
        val strategy1 = ImageLoadStrategy.Memory(memoryCache, strategy2)

        var actualImage: Bitmap? = null
        val lock = CountDownLatch(1)
        strategy1.proceed("url", TargetSize(100, 100), {
            actualImage = it
            lock.countDown()
        })

        lock.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == image.width && actualImage!!.height == image.height)

        // should be stored in memory cache
        Assert.assertEquals(actualImage, memoryCache.get("url"))

        cache.clear()
        memoryCache.clear()
    }

    @Test
    fun testMemoryPersistenceNetworkChain() {
        val image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        val context = ApplicationProvider.getApplicationContext<Context>()

        val strategy3 = createNetworkLoadStrategy(image)

        val cache = ImageCache.FileImageCache(
            context,
            KeyHash.MD5(),
            ImageDecoder.FileDecoder(),
            "testcache"
        )
        val strategy2 =
            ImageLoadStrategy.Persistence(cache, Executors.newSingleThreadExecutor(), strategy3)

        val memoryCache = ImageCache.MemoryImageCache()
        val strategy1 = ImageLoadStrategy.Memory(memoryCache, strategy2)

        var actualImage: Bitmap? = null
        val lock = CountDownLatch(1)
        strategy1.proceed("https://goodurl", TargetSize(100, 100), {
            actualImage = it
            lock.countDown()
        })

        lock.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(actualImage != null)
        Assert.assertTrue(actualImage!!.width == image.width && actualImage!!.height == image.height)

        // should be stored in storage cache
        val cachedStorageImage = cache.get("https://goodurl")
        Assert.assertTrue(cachedStorageImage != null)
        Assert.assertTrue(cachedStorageImage!!.width == image.width && cachedStorageImage.height == image.height)

        // should be stored in memory cache
        val cachedMemoryImage = cache.get("https://goodurl")
        Assert.assertTrue(cachedMemoryImage != null)
        Assert.assertTrue(cachedMemoryImage!!.width == image.width && cachedMemoryImage.height == image.height)

        cache.clear()
        memoryCache.clear()
    }

    private fun createNetworkLoadStrategy(image: Bitmap?): ImageLoadStrategy.Remote {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return ImageLoadStrategy.Remote(
            object : OkHttpClient() {
                override fun newCall(request: Request): Call {
                    return object : Call {
                        override fun cancel() = Unit
                        override fun clone(): Call = this
                        override fun enqueue(responseCallback: Callback) {
                            val stream = ByteArrayOutputStream()
                            image?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val byteArray: ByteArray = stream.toByteArray()
                            responseCallback.onResponse(
                                this, Response.Builder()
                                    .request(Request.Builder().url("https://mocked.com").build())
                                    .code(200)
                                    .message("")
                                    .protocol(Protocol.HTTP_1_1)
                                    .body(byteArray.toResponseBody())
                                    .build()
                            )
                        }

                        override fun execute(): Response = throw NotImplementedError()
                        override fun isCanceled(): Boolean = false
                        override fun isExecuted(): Boolean = true
                        override fun request(): Request = Request.Builder().build()
                        override fun timeout(): Timeout = Timeout()

                    }
                }
            },
            ImageDecoder.StreamDecoder(TempFile.Cache(context), ComputeScale.KeepAspectRatio())
        )
    }
}