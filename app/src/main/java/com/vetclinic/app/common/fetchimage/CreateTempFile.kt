package com.vetclinic.app.common.fetchimage

import android.content.Context
import java.io.File

interface CreateTempFile {
    fun create(): File

    class Cache(context: Context) : CreateTempFile {
        private val cacheDir = context.cacheDir

        override fun create(): File {
            return File.createTempFile("tmp", null, cacheDir).apply {
                deleteOnExit()
            }
        }
    }
}
