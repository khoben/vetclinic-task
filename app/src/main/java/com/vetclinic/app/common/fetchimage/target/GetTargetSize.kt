package com.vetclinic.app.common.fetchimage.target

import android.view.View
import androidx.core.view.doOnPreDraw

interface GetTargetSize<T> {
    fun onSizeReady(target: T, callback: (size: TargetSize) -> Unit)

    class ViewTarget : GetTargetSize<View> {
        override fun onSizeReady(target: View, callback: (size: TargetSize) -> Unit) {
            if (target.width != 0 && target.height != 0) {
                callback.invoke(TargetSize(target.width, target.height))
            }

            target.doOnPreDraw {
                callback.invoke(TargetSize(it.width, it.height))
            }
        }
    }
}