package com.vetclinic.app.common.async

import org.junit.Assert
import org.junit.Test

class CombinedCallbackTest {

    @Test
    fun combinedCallbackShouldRunCallbacksOnCollect() {
        var callbackCalled = 0
        val successCallback = fun(onResult: (Unit) -> Unit, _: (Throwable) -> Unit) {
            onResult.invoke(Unit)
            callbackCalled++
        }
        val combinedCallback = CombinedCallback(
            successCallback,
            successCallback
        )
        combinedCallback.invoke({}, { throw it })
        Assert.assertEquals(2, callbackCalled)
    }

    @Test
    fun combinedCallbackShouldCollected() {
        val successCallback = fun(onResult: (Unit) -> Unit, _: (Throwable) -> Unit) {
            onResult.invoke(Unit)
        }
        val combinedCallback = CombinedCallback(
            successCallback,
            successCallback
        )
        var isCollected = false
        combinedCallback.invoke({
            isCollected = true
        }, {
            throw it
        })
        Assert.assertTrue(isCollected)
    }

    @Test(expected = CombinedExpectedException::class)
    fun combinedCallbackShouldFailedAndNotCollected() {
        val failedCallback = fun(_: (Unit) -> Unit, onError: (Throwable) -> Unit) {
            onError.invoke(CombinedExpectedException())
        }
        val combinedCallback = CombinedCallback(
            failedCallback,
            failedCallback
        )
        combinedCallback.invoke({
            throw CombinedShouldNotCollectedException()
        }, {
            throw it
        })
    }

    @Test
    fun combinedCallbackFailShouldCalledOnce() {
        var callbackCalled = 0
        val failedCallback = fun(_: (Unit) -> Unit, onError: (Throwable) -> Unit) {
            onError.invoke(CombinedExpectedException())
        }
        val combinedCallback = CombinedCallback(
            failedCallback,
            failedCallback
        )
        combinedCallback.invoke({
            throw CombinedShouldNotCollectedException()
        }, {
            callbackCalled++
        })
        Assert.assertEquals(1, callbackCalled)
    }

    @Test(expected = CombinedExpectedException::class)
    fun combinedCallbackShouldFailedAndNotCollectedOnFirstFailed() {
        val failedCallback = fun(_: (Unit) -> Unit, onError: (Throwable) -> Unit) {
            onError.invoke(CombinedExpectedException())
        }
        val successCallback = fun(onResult: (Unit) -> Unit, _: (Throwable) -> Unit) {
            onResult.invoke(Unit)
        }
        val combinedCallback = CombinedCallback(
            failedCallback,
            successCallback
        )
        combinedCallback.invoke({
            throw CombinedShouldNotCollectedException()
        }, {
            throw it
        })
    }

    @Test(expected = CombinedExpectedException::class)
    fun combinedCallbackShouldFailedAndNotCollectedOnSecondFailed() {
        val failedCallback = fun(_: (Unit) -> Unit, onError: (Throwable) -> Unit) {
            onError.invoke(CombinedExpectedException())
        }
        val successCallback = fun(onResult: (Unit) -> Unit, _: (Throwable) -> Unit) {
            onResult.invoke(Unit)
        }
        val combinedCallback = CombinedCallback(
            successCallback,
            failedCallback
        )
        combinedCallback.invoke({
            throw CombinedShouldNotCollectedException()
        }, {
            throw it
        })
    }
}