package com.vetclinic.app.common.observer

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting

class SingleEventLiveData<T>(
    private val mainHandler: Handler = Handler(Looper.getMainLooper())
) : MutableLiveData<T> {

    @VisibleForTesting
    var lastEmittedData: T? = null

    private var data: T? = null
    private var observer: ((T) -> Unit) = {}

    override fun observe(observer: (T) -> Unit) {
        this.observer = observer
    }

    override fun emit(data: T) {
        lastEmittedData = data
        this.data = data
        mainHandler.post {
            observer.invoke(data)
        }
    }
}