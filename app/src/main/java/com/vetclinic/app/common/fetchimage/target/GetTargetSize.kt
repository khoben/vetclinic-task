package com.vetclinic.app.common.fetchimage.target

import android.util.Size
import android.view.View
import androidx.core.view.doOnPreDraw

interface GetTargetSize<T> {
    fun onSizeReady(target: T, callback: (size: Size) -> Unit)

    class ViewTarget : GetTargetSize<View> {
        override fun onSizeReady(target: View, callback: (size: Size) -> Unit) {
            if (target.width != 0 && target.height != 0) {
                callback.invoke(Size(target.width, target.height))
            }

            target.doOnPreDraw {
                callback.invoke(Size(it.width, it.height))
            }
        }
    }
}