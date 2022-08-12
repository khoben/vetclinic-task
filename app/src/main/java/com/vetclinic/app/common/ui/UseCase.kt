package com.vetclinic.app.common.ui

interface UseCase<R> {
    operator fun invoke(onResult: (R) -> Unit, onError: (Throwable) -> Unit)
}