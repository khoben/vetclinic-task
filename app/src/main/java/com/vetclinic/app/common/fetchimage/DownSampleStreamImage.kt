package com.vetclinic.app.common.fetchimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Size
import com.vetclinic.app.common.fetchimage.exception.DownSampleException
import java.io.InputStream

interface DownSampleStreamImage {

    fun downSampleImageFromStream(imageStream: InputStream, targetSize: Size): Bitmap?

    class Base(private val createTempFile: CreateTempFile) : DownSampleStreamImage {

        override fun downSampleImageFromStream(
            imageStream: InputStream,
            targetSize: Size
        ): Bitmap? {
            try {
                val tmp = createTempFile.create()
                tmp.outputStream().use { imageStream.copyTo(it) }

                val bitmapOpts = BitmapFactory.Options()
                tmp.inputStream().use {
                    bitmapOpts.inJustDecodeBounds = true
                    BitmapFactory.decodeStream(it, null, bitmapOpts)
                }

                var sourceWidth = bitmapOpts.outWidth
                var sourceHeight = bitmapOpts.outHeight
                var targetScale = 1
                while (sourceWidth / 2 > targetSize.width && sourceHeight / 2 > targetSize.height) {
                    sourceWidth /= 2
                    sourceHeight /= 2
                    targetScale *= 2
                }

                bitmapOpts.inJustDecodeBounds = false
                bitmapOpts.inSampleSize = targetScale

                val decodedBitmap = tmp.inputStream().use {
                    BitmapFactory.decodeStream(
                        it,
                        null,
                        bitmapOpts
                    )
                }
                return decodedBitmap
            } catch (e: Exception) {
                throw DownSampleException(e)
            }
        }
    }
}