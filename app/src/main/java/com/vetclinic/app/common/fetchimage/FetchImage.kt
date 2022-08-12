package com.vetclinic.app.common.fetchimage

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.vetclinic.app.common.fetchimage.exception.UnsuccessfulImageRequest
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.util.concurrent.ExecutorService


interface FetchImage {

    fun into(view: ImageView, url: String, onError: (e: Throwable) -> Unit = {})

    fun cancel(view: ImageView)

    /**
     * TODOs:
     * 1. Request cancellation
     * 2. Check if request already running-in
     * 3. Image loading strategy (CoR or smth)
     */
    class Base(
        @DrawableRes private val placeholder: Int,
        private val okHttpClient: OkHttpClient,
        private val memoryCache: ImageCache.Memory,
        private val persistenceCache: ImageCache.Persistence,
        private val downSampleImage: DownSampleStreamImage,
        private val executorService: ExecutorService,
        private val mainHandler: Handler = Handler(Looper.getMainLooper())
    ) : FetchImage {

        override fun into(view: ImageView, url: String, onError: (e: Throwable) -> Unit) {
            val memoryCachedImage = memoryCache.get(url)
            if (memoryCachedImage != null) {
                Timber.d("From memory: $url")
                view.setImageBitmap(memoryCachedImage)
                return
            }

            executorService.submit {
                val image = try {
                    persistenceCache.get(url)?.also {
                        Timber.d("From storage: $url")
                        memoryCache.store(url, it)
                    } ?: fetchImage(url, Size(view.width, view.height))
                        ?.also {
                            Timber.d("From net: $url")
                            memoryCache.store(url, it)
                            persistenceCache.store(url, it)
                        }
                } catch (e: Exception) {
                    onError.invoke(e)
                    Timber.e(e)
                    null
                }

                if (image != null) {
                    mainHandler.post { view.setImageBitmap(image) }
                } else {
                    mainHandler.post { view.setImageResource(placeholder) }
                }
            }
        }

        override fun cancel(view: ImageView) {

        }

        private fun fetchImage(
            url: String,
            targetSize: Size
        ): Bitmap? {
            val request: Request = Request.Builder().url(url).build()
            okHttpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body!!.byteStream().use { imageStream ->
                        return downSampleImage.downSampleImageFromStream(
                            imageStream,
                            targetSize
                        )
                    }
                } else {
                    throw UnsuccessfulImageRequest(url)
                }
            }
        }
    }
}