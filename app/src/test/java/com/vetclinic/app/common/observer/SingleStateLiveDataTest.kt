package com.vetclinic.app.common.observer

import com.vetclinic.app.common.ui.UiExecutor
import com.vetclinic.app.testing.getOrAwaitValue
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class SingleStateLiveDataTest {

    @Test
    fun testObservedStateData() {
        val emittedData = 213
        val livedata = SingleStateLiveData<Int>(emittedData, UiExecutor.Test())
        Assert.assertEquals(emittedData, livedata.asDataObserver().getOrAwaitValue())
    }

    @Test
    fun testObservedData() {
        val livedata = SingleStateLiveData<Int>(321, UiExecutor.Test())
        val emittedData = 213
        livedata.emit(emittedData)
        Assert.assertEquals(emittedData, livedata.asDataObserver().getOrAwaitValue())
    }

    @Test(expected = TimeoutException::class)
    fun testRemovedObserverShouldNotBeObserver() {
        val initialData = 312
        val livedata = SingleStateLiveData<Int>(initialData, UiExecutor.Test())
        var secondEmitting = false
        livedata.observe {
            if (!secondEmitting) {
                secondEmitting = true
                Assert.assertEquals(initialData, it)
            } else {
                throw RuntimeException("Shouldn't be collected")
            }
        }
        livedata.removeObserver()
        val emittedData = 213
        livedata.emit(emittedData)
        val latch = CountDownLatch(1)
        if (!latch.await(2000, TimeUnit.MILLISECONDS)) {
            throw TimeoutException("DataObserver value was never set.")
        }
    }
}