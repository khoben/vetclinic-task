package com.vetclinic.app.common.async

typealias Callback<V> = (onResult: (V) -> Unit, onError: (Throwable) -> Unit) -> Unit