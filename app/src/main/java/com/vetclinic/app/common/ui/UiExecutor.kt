package com.vetclinic.app.common.ui

import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat

interface UiExecutor {
    fun execute(block: () -> Unit)

    class Main(
        private val uiHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    ) : UiExecutor {

        override fun execute(block: () -> Unit) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                uiHandler.post(block)
            } else {
                block.invoke()
            }
        }
    }

    class Test : UiExecutor {
        override fun execute(block: () -> Unit) {
            block.invoke()
        }
    }
}