package com.vetclinic.app.common.observer

import com.vetclinic.app.common.ui.UiExecutor
import com.vetclinic.app.common.ui.WithLifecycle

class SingleEventLiveData<T>(
    private val uiExecutor: UiExecutor
) : MutableLiveData<T> {

    private var data: T? = null
    private var observer: ((T) -> Unit)? = null
    private var unconsumedData: T? = null

    override fun observe(observer: (T) -> Unit): WithLifecycle {
        this.observer = observer
        unconsumedData?.let {
            uiExecutor.execute { observer.invoke(it) }
            unconsumedData = null
        }
        return this
    }

    override fun emit(data: T) {
        this.data = data
        val currentObserver = observer
        if (currentObserver != null) {
            uiExecutor.execute { currentObserver.invoke(data) }
        } else {
            unconsumedData = data
        }
    }

    override fun removeObserver() {
        this.observer = null
    }

    override fun onDestroy() {
        removeObserver()
    }
}