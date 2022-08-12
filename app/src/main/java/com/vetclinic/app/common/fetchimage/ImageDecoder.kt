package com.vetclinic.app.common.fetchimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import timber.log.Timber
import java.io.File
import java.io.FileInputStream

interface ImageDecoder<T> {
    fun decode(source: T): Bitmap?

    class FileDecoder : ImageDecoder<File?> {
        override fun decode(source: File?): Bitmap? {
            if (source == null || !source.exists()) return null
            try {
                return FileInputStream(source).use { BitmapFactory.decodeStream(it) }
            } catch (e: Exception) {
                Timber.e(e)
            }
            return null
        }
    }
}