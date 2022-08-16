package com.vetclinic.app.common.fetchimage.decode

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.vetclinic.app.common.fetchimage.exception.ImageDecoderException
import com.vetclinic.app.common.fetchimage.target.ComputeScale
import com.vetclinic.app.common.fetchimage.target.TargetSize
import org.junit.Assert
import org.junit.Test
import java.io.InputStream


class StreamImageDecoderTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val decoder = ImageDecoder.StreamDecoder(
        TempFile.Cache(context),
        ComputeScale.KeepAspectRatio()
    )

    @Test(expected = NotImplementedError::class)
    fun testDecoderWithoutScale() {
        decoder.decode(object : InputStream() {
            override fun read(): Int = -1
        })
    }

    @Test(expected = ImageDecoderException::class)
    fun testDecoderNotExistsSource() {
        decoder.decode(object : InputStream() {
            override fun read(): Int = -1
        }, TargetSize(1, 1))
    }

    @Test
    fun testDecoderSource() {
        val decoded = decoder.decode(
            context.assets.open("test-image.jpg"),
            TargetSize(60, 60)
        )

        Assert.assertTrue(decoded != null)
        Assert.assertTrue(decoded!!.width == 60 && decoded.height == 60)
    }
}