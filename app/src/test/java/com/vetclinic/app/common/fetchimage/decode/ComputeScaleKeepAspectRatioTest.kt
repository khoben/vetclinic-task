package com.vetclinic.app.common.fetchimage.decode

import com.vetclinic.app.common.fetchimage.target.ComputeScale
import com.vetclinic.app.common.fetchimage.target.TargetSize
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ComputeScaleKeepAspectRatioTest(
    private val sourceSize: TargetSize,
    private val targetSize: TargetSize,
    private val expectedScale: Int
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters(): Iterable<Array<Any>> {
            return listOf(
                arrayOf(
                    TargetSize(1, 1),
                    TargetSize(1, 1),
                    1
                ),
                arrayOf(
                    TargetSize(256, 256),
                    TargetSize(100, 100),
                    2
                ),
                arrayOf(
                    TargetSize(256, 100),
                    TargetSize(100, 100),
                    2
                ),
                arrayOf(
                    TargetSize(256, 1),
                    TargetSize(100, 100),
                    2
                ),
            )
        }
    }

    private val scale = ComputeScale.KeepAspectRatio()

    @Test
    fun testKeepAspectRatio() {
        val actualScale = scale.getScale(sourceSize, targetSize)
        Assert.assertEquals(expectedScale, actualScale)
    }
}