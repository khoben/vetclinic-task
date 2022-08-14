package com.vetclinic.app.common.fetchimage

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.vetclinic.app.common.fetchimage.cache.ImageCache
import com.vetclinic.app.common.fetchimage.decode.ImageDecoder
import com.vetclinic.app.common.fetchimage.exception.UnsuccessfulImageRequest
import com.vetclinic.app.common.fetchimage.target.GetTargetSize
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.InputStream
import java.util.concurrent.ExecutorService


interface FetchImage {

    @MainThread
    fun into(view: ImageView, url: String, onError: (e: Throwable) -> Unit = {})

    @MainThread
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
        private val getTargetSize: GetTargetSize<View>,
        private val memoryCache: ImageCache.Memory,
        private val persistenceCache: ImageCache.Persistence,
        private val streamImageDecoder: ImageDecoder<InputStream>,
        private val executorService: ExecutorService,
        private val mainHandler: Handler = Handler(Looper.getMainLooper())
    ) : FetchImage {

        @MainThread
        override fun into(view: ImageView, url: String, onError: (e: Throwable) -> Unit) {
            val memoryCachedImage = memoryCache.get(url)
            if (memoryCachedImage != null) {
                Timber.d("From memory: $url")
                view.setImageBitmap(memoryCachedImage)
                return
            }

            view.setImageResource(placeholder)

            getTargetSize.onSizeReady(view) { targetSize ->
                Timber.d("TargetSize = $targetSize")
                executorService.submit {
                    val image = try {
                        persistenceCache.get(url)?.also {
                            Timber.d("From storage: $url")
                            memoryCache.store(url, it)
                        } ?: fetchImage(url, targetSize)
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
        }

        @MainThread
        override fun cancel(view: ImageView) {

        }

        @WorkerThread
        private fun fetchImage(
            url: String,
            targetSize: Size
        ): Bitmap? {
            val request: Request = Request.Builder().url(url).build()
            okHttpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body!!.byteStream().use { imageStream ->
                        return streamImageDecoder.decode(
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