package com.vetclinic.app.common.observer

interface DataObserver<T> {
    fun observe(observer: (T) -> Unit)
}