package com.vetclinic.app.common.fetchimage.decode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.vetclinic.app.common.fetchimage.exception.ImageDecoderException
import com.vetclinic.app.common.fetchimage.target.ComputeScale
import com.vetclinic.app.common.fetchimage.target.TargetSize
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

interface ImageDecoder<T> {
    fun decode(source: T): Bitmap?

    fun decode(source: T, targetSize: TargetSize): Bitmap?

    class FileDecoder : ImageDecoder<File> {
        override fun decode(source: File): Bitmap? {
            try {
                return FileInputStream(source).use { BitmapFactory.decodeStream(it) }
            } catch (e: Exception) {
                throw ImageDecoderException(e)
            }
        }

        override fun decode(source: File, targetSize: TargetSize): Bitmap? {
            throw NotImplementedError(
                "FileDecoder can read only origin size, use FileDecoder.decode(File?)"
            )
        }
    }

    class StreamDecoder(
        private val tempFile: TempFile,
        private val computeScale: ComputeScale
    ) : ImageDecoder<InputStream> {

        override fun decode(source: InputStream): Bitmap? {
            throw NotImplementedError(
                "StreamDecoder can read only scaled size, " +
                        "use FileDecoder.decode(InputStream, ImageSize)"
            )
        }

        override fun decode(source: InputStream, targetSize: TargetSize): Bitmap? {
            try {
                val tmp = tempFile.create()
                tmp.outputStream().use { source.copyTo(it) }

                val sizeBitmapOpts = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }

                tmp.inputStream().use {
                    BitmapFactory.decodeStream(it, null, sizeBitmapOpts)
                }

                val targetScale =
                    computeScale.getScale(
                        TargetSize(sizeBitmapOpts.outWidth, sizeBitmapOpts.outHeight),
                        targetSize
                    )

                val outputBitmapsOpts = BitmapFactory.Options().apply {
                    inSampleSize = targetScale
                }

                val decodedBitmap = tmp.inputStream().use {
                    BitmapFactory.decodeStream(
                        it,
                        null,
                        outputBitmapsOpts
                    )
                }
                tmp.delete()
                return decodedBitmap
            } catch (e: Exception) {
                throw ImageDecoderException(e)
            }
        }
    }
}