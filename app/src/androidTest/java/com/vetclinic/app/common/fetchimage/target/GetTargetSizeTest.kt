package com.vetclinic.app.common.fetchimage.target

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class GetTargetSizeTest {

    private val getTargetSize = GetTargetSize.ViewTarget()

    private lateinit var context: Context
    private lateinit var view: View

    @Before
    fun before() {
        context = ApplicationProvider.getApplicationContext()
        view = View(context)
    }

    @Test
    fun testViewAlreadyMeasured() {
        view.layoutParams = ViewGroup.LayoutParams(160, 160)
        view.measure(160, 160)
        view.layout(0, 0, 160, 160)
        view.viewTreeObserver.dispatchOnPreDraw()

        var actualSize = TargetSize(0, 0)

        val lock = CountDownLatch(1)
        getTargetSize.onSizeReady(view) {
            actualSize = it
            lock.countDown()
        }

        lock.await(2000, TimeUnit.MILLISECONDS)
        Assert.assertEquals(TargetSize(160, 160), actualSize)
    }

    @Test
    fun testViewNotMeasured() {
        var actualSize = TargetSize(0, 0)

        val lock = CountDownLatch(1)
        getTargetSize.onSizeReady(view) {
            actualSize = it
            lock.countDown()
        }

        view.layoutParams = ViewGroup.LayoutParams(160, 160)
        view.measure(160, 160)
        view.layout(0, 0, 160, 160)
        view.viewTreeObserver.dispatchOnPreDraw()

        lock.await(2000, TimeUnit.MILLISECONDS)
        Assert.assertEquals(TargetSize(160, 160), actualSize)
    }

    @Test
    fun testEnsureGetSizeAtPreDraw() {
        var actualSize = TargetSize(0, 0)

        val lock = CountDownLatch(1)
        getTargetSize.onSizeReady(view) {
            actualSize = it
            lock.countDown()
        }

        view.layoutParams = ViewGroup.LayoutParams(160, 160)
        view.measure(160, 160)
        view.layout(0, 0, 160, 160)

        lock.await(2000, TimeUnit.MILLISECONDS)
        Assert.assertEquals(TargetSize(0, 0), actualSize)
    }
}