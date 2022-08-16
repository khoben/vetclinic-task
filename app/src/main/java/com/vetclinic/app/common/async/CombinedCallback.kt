package com.vetclinic.app.common.async

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class CombinedCallback<F, S>(
    private val first: Callback<F>,
    private val second: Callback<S>
) {
    private val remaining = AtomicInteger(2)
    private val failed = AtomicBoolean(false)

    private val firstResult = AtomicReference<F>()
    private val secondResult = AtomicReference<S>()

    operator fun invoke(
        onResult: (Pair<F, S>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        invokeForResult(first, firstResult, onResult, onError)
        invokeForResult(second, secondResult, onResult, onError)
    }

    private fun <T> invokeForResult(
        callback: Callback<T>,
        result: AtomicReference<T>,
        onResult: (Pair<F, S>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        callback.invoke({
            result.set(it)
            if (remaining.decrementAndGet() == 0 && !failed.get()) {
                onResult.invoke(Pair(firstResult.get(), secondResult.get()))
            }
        }, {
            if (failed.compareAndSet(false, true)) {
                onError.invoke(it)
            }
        })
    }
}