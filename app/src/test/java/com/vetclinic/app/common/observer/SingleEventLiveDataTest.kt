package com.vetclinic.app.common.observer

import com.vetclinic.app.common.ui.UiExecutor
import com.vetclinic.app.testing.getOrAwaitValue
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class SingleEventLiveDataTest {

    @Test
    fun testObservedData() {
        val livedata = SingleEventLiveData<Int>(UiExecutor.Test())
        val emittedData = 213
        livedata.emit(emittedData)
        Assert.assertEquals(emittedData, livedata.asDataObserver().getOrAwaitValue())
    }

    @Test(expected = TimeoutException::class)
    fun testRemovedObserverShouldNotBeObserver() {
        val livedata = SingleEventLiveData<Int>(UiExecutor.Test())
        val emittedData = 213
        livedata.observe {
            throw RuntimeException("Shouldn't be collected")
        }
        livedata.removeObserver()
        livedata.emit(emittedData)
        val latch = CountDownLatch(1)
        if (!latch.await(2000, TimeUnit.MILLISECONDS)) {
            throw TimeoutException("DataObserver value was never set.")
        }
    }
}