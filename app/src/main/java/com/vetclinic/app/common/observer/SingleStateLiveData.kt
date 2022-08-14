package com.vetclinic.app.common.observer

import com.vetclinic.app.common.ui.UiExecutor
import android.os.Looper

class SingleStateLiveData<T>(
    initial: T,
    private val uiExecutor: UiExecutor
) : MutableLiveData<T> {

    private var data: T = initial
    private var observer: ((T) -> Unit) = {}

    val value get() = data

    override fun observe(observer: (T) -> Unit) {
        this.observer = observer
        mainHandler.post { observer.invoke(data) }
    }

    override fun emit(data: T) {
        this.data = data
        mainHandler.post { observer.invoke(data) }
    }
}