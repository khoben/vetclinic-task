package com.vetclinic.app.common.fetchimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import com.vetclinic.app.R
import com.vetclinic.app.common.fetchimage.strategy.ImageLoadStrategy
import com.vetclinic.app.common.fetchimage.target.GetTargetSize
import com.vetclinic.app.common.fetchimage.target.TargetSize
import com.vetclinic.app.common.ui.UiExecutor
import com.vetclinic.app.testing.mock.MockedException
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FetchImageTest {

    @Test
    fun testFetchImage() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val image = Bitmap.createBitmap(
            100,
            100,
            Bitmap.Config.ARGB_8888
        )

        val fetchImage = FetchImage.Base(
            R.drawable.ic_launcher_foreground,
            loadStrategy = object : ImageLoadStrategy {
                override fun proceed(
                    url: String,
                    targetSize: TargetSize,
                    onResult: (result: Bitmap?) -> Unit,
                    onBackwardResult: (result: Bitmap) -> Unit
                ) {
                    onResult.invoke(image)
                }
            }, object : GetTargetSize<View> {
                override fun onSizeReady(target: View, callback: (size: TargetSize) -> Unit) {
                    callback.invoke(TargetSize(target.width, target.height))
                }
            }, UiExecutor.Test()
        )

        val imageView = ImageView(context)

        imageView.layoutParams = ViewGroup.LayoutParams(160, 160)
        imageView.measure(160, 160)
        imageView.layout(0, 0, 160, 160)
        imageView.viewTreeObserver.dispatchOnPreDraw()

        fetchImage.into(imageView, "url")

        val lock = CountDownLatch(1)
        lock.await(2000, TimeUnit.MILLISECONDS)

        val actual = (imageView.drawable as? BitmapDrawable)?.bitmap

        Assert.assertEquals(image, actual)
    }

    @Test
    fun testFetchNullImage() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val fetchImage = FetchImage.Base(
            R.drawable.ic_launcher_foreground,
            loadStrategy = object : ImageLoadStrategy {
                override fun proceed(
                    url: String,
                    targetSize: TargetSize,
                    onResult: (result: Bitmap?) -> Unit,
                    onBackwardResult: (result: Bitmap) -> Unit
                ) {
                    onResult.invoke(null)
                }
            }, object : GetTargetSize<View> {
                override fun onSizeReady(target: View, callback: (size: TargetSize) -> Unit) {
                    callback.invoke(TargetSize(target.width, target.height))
                }
            }, UiExecutor.Test()
        )

        val imageView = ImageView(context)

        imageView.layoutParams = ViewGroup.LayoutParams(160, 160)
        imageView.measure(160, 160)
        imageView.layout(0, 0, 160, 160)
        imageView.viewTreeObserver.dispatchOnPreDraw()
        imageView.setImageDrawable(null)

        fetchImage.into(imageView, "url")

        val lock = CountDownLatch(1)
        lock.await(2000, TimeUnit.MILLISECONDS)

        // has placeholder drawable
        Assert.assertTrue(imageView.drawable != null)
    }

    @Test
    fun testFetchErrorCallback() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val fetchImage = FetchImage.Base(
            R.drawable.ic_launcher_foreground,
            loadStrategy = object : ImageLoadStrategy {
                override fun proceed(
                    url: String,
                    targetSize: TargetSize,
                    onResult: (result: Bitmap?) -> Unit,
                    onBackwardResult: (result: Bitmap) -> Unit
                ) {
                    throw MockedException()
                }
            }, object : GetTargetSize<View> {
                override fun onSizeReady(target: View, callback: (size: TargetSize) -> Unit) {
                    callback.invoke(TargetSize(target.width, target.height))
                }
            }, UiExecutor.Test()
        )

        val imageView = ImageView(context)

        imageView.layoutParams = ViewGroup.LayoutParams(160, 160)
        imageView.measure(160, 160)
        imageView.layout(0, 0, 160, 160)
        imageView.viewTreeObserver.dispatchOnPreDraw()
        imageView.setImageDrawable(null)

        var actualException: Throwable = RuntimeException()
        fetchImage.into(imageView, "url") {
            actualException = it
        }

        val lock = CountDownLatch(1)
        lock.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(actualException is MockedException)
    }
}