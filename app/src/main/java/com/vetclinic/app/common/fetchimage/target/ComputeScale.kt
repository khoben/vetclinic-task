package com.vetclinic.app.common.fetchimage.target


interface ComputeScale {
    fun getScale(source: TargetSize, target: TargetSize): Int

    class KeepAspectRatio : ComputeScale {
        override fun getScale(source: TargetSize, target: TargetSize): Int {

            if (source.width <= 0 || source.height <= 0 ||
                target.width <= 0 || target.height <= 0
            ) {
                throw ComputeScaleIllegalStateException()
            }

            return if (source.width > source.height) {
                source.width / target.width
            } else {
                source.height / target.height
            }
        }
    }

    class Factor2 : ComputeScale {
        override fun getScale(source: TargetSize, target: TargetSize): Int {

            if (source.width <= 0 || source.height <= 0 ||
                target.width <= 0 || target.height <= 0
            ) {
                throw ComputeScaleIllegalStateException()
            }

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
