package com.vetclinic.app.common.fetchimage.decode

import android.content.Context
import java.io.File

interface TempFile {
    fun create(): File

    class Cache(context: Context) : TempFile {
        private val cacheDir = context.cacheDir

        override fun create(): File {
            return File.createTempFile("tmp", null, cacheDir).apply {
                deleteOnExit()
            }
        }
    }
}
