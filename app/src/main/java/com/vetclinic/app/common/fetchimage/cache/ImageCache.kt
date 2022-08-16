package com.vetclinic.app.common.fetchimage.cache

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.vetclinic.app.common.extensions.deleteRecursivelyWithoutRoot
import com.vetclinic.app.common.fetchimage.decode.ImageDecoder
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


interface ImageCache {

    fun get(key: String): Bitmap?
    fun store(key: String, image: Bitmap)
    fun clear()

    interface Memory : ImageCache
    interface Persistence : ImageCache

    class FileImageCache(
        context: Context,
        private val keyHash: KeyHash,
        private val imageDecoder: ImageDecoder<File>,
        dirName: String = "imgcache"
    ) : Persistence {

        private val cacheFolder: File

        init {
            cacheFolder = File(context.cacheDir, dirName)
            if (!cacheFolder.exists()) {
                cacheFolder.mkdirs()
            }
        }

        override fun get(key: String): Bitmap? {
            val file = File(cacheFolder, keyHash.hash(key))
            return if (file.exists()) return imageDecoder.decode(file) else null
        }

        override fun store(key: String, image: Bitmap) {
            try {
                val file = File(cacheFolder, keyHash.hash(key))
                if (file.exists()) {
                    file.delete()
                }
                FileOutputStream(file).use {
                    image.compress(Bitmap.CompressFormat.JPEG, DEFAULT_QUALITY, it)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        override fun clear() {
            cacheFolder.deleteRecursivelyWithoutRoot()
        }

        companion object {
            private const val DEFAULT_QUALITY = 100
        }
    }

    class MemoryImageCache : Memory {

        private val cacheSize = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
        private val cache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }

        override fun get(key: String): Bitmap? {
            return cache.get(key)
        }

        override fun store(key: String, image: Bitmap) {
            cache.put(key, image)
        }

        override fun clear() {
            cache.evictAll()
        }
    }
}