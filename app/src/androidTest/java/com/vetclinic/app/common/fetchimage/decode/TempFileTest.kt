package com.vetclinic.app.common.fetchimage.decode

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Test

class TempFileTest {

    @Test
    fun testTempFileCreatedInCacheFolder() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val tempFile = TempFile.Cache(context)
        val file = tempFile.create()
        Assert.assertEquals(context.cacheDir.path, file.parentFile.path)
        Assert.assertTrue(file.exists() && file.isFile)
        file.delete()
    }
}