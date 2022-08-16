package com.vetclinic.app.common.fetchimage.decode

import com.vetclinic.app.common.fetchimage.target.ComputeScale
import com.vetclinic.app.common.fetchimage.target.ComputeScaleIllegalStateException
import com.vetclinic.app.common.fetchimage.target.TargetSize
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ComputeScaleFactor2FailTest(
    private val size: TargetSize,
    private val targetSize: TargetSize
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters(): Iterable<Array<Any>> {
            return listOf(
                arrayOf(
                    TargetSize(0, 1),
                    TargetSize(1, 1),
                ),
                arrayOf(
                    TargetSize(100, 0),
                    TargetSize(300, 200),
                ),
                arrayOf(
                    TargetSize(500, 1000),
                    TargetSize(-1, 100),
                ),
                arrayOf(
                    TargetSize(500, 1000),
                    TargetSize(400, 0),
                ),
            )
        }
    }

    private val scale = ComputeScale.Factor2()

    @Test(expected = ComputeScaleIllegalStateException::class)
    fun testFactor2Scale() {
        scale.getScale(size, targetSize)
    }
}