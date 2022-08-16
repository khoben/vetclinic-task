package com.vetclinic.app.common.fetchimage.decode

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.vetclinic.app.common.fetchimage.exception.ImageDecoderException
import com.vetclinic.app.common.fetchimage.target.TargetSize
import org.junit.Assert
import org.junit.Test
import java.io.File

class FileImageDecoderTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val decoder = ImageDecoder.FileDecoder()

    @Test(expected = NotImplementedError::class)
    fun testDecoderWithScale() {
        decoder.decode(File(""), TargetSize(1, 1))
    }

    @Test(expected = ImageDecoderException::class)
    fun testDecoderNotExistsSource() {
        val decoded = decoder.decode(File(""))
        Assert.assertTrue(decoded == null)
    }

    @Test
    fun testDecoderSource() {
        val file = File("${context.cacheDir}/test-image.jpg").apply {
            writeBytes(context.assets.open("test-image.jpg").readBytes())
        }
        val decoded = decoder.decode(file)

        Assert.assertTrue(decoded != null)
        Assert.assertTrue(decoded!!.width == 300 && decoded.height == 300)

        file.delete()
    }
}