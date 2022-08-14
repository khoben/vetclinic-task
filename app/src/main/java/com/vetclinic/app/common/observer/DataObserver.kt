package com.vetclinic.app.common.observer

import com.vetclinic.app.common.ui.WithLifecycle

interface DataObserver<T> : WithLifecycle {
    fun observe(observer: (T) -> Unit): WithLifecycle
    fun removeObserver()
}