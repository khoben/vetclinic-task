package com.vetclinic.app.common.observer

interface DataEmitter<T> {
    fun emit(data: T)
}