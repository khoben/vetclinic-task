package com.vetclinic.app.common.observer

interface MutableLiveData<T> : DataObserver<T>, DataEmitter<T> {
    fun asDataObserver(): DataObserver<T> = this as DataObserver<T>
}