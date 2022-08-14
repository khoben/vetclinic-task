package com.vetclinic.app.common.fetchimage.decode

import android.util.Size

interface ComputeScale {
    fun getScale(source: Size, target: Size): Int

    class Factor2 : ComputeScale {
        override fun getScale(source: Size, target: Size): Int {
            var sourceWidth = source.width
            var sourceHeight = source.height
            var targetScale = 1

            while (sourceWidth / 2 > target.width && sourceHeight / 2 > target.height) {
                sourceWidth /= 2
                sourceHeight /= 2
                targetScale *= 2
            }

            return targetScale
        }
    }
}
