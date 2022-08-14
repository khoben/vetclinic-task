package com.vetclinic.app.common.observer

import com.vetclinic.app.common.ui.UiExecutor
import android.os.Looper

class SingleEventLiveData<T>(
    private val uiExecutor: UiExecutor
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