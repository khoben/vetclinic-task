package com.vetclinic.app.data.cloud

import com.vetclinic.app.common.network.exception.BadHttpServiceCall
import com.vetclinic.app.testing.mockOkHttpClient
import org.json.JSONException
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.*


class ConfigHttpServiceTest {

    private val configUrl = "https://test/config"

    @Test
    fun testConfigServiceSuccess() {

        val configHttpService = ConfigHttpService(
            url = configUrl,
            unmarshallResponse = UnmarshallConfigResponse(),
            client = mockOkHttpClient(
                configUrl,
                "{\"settings\":{\"isChatEnabled\":true,\"isCallEnabled\":true," +
                        "\"workHours\":\"M-F 9:00 - 18:00\"}}"
            )
        )
        val expectedCloudModel = ConfigCloud(
            isChatEnabled = true,
            isCallEnabled = true,
            workHours = "M-F 9:00 - 18:00"
        )
        var actualCloudModel: ConfigCloud? = null

        val countDownLatch = CountDownLatch(1)
        configHttpService.get({
            actualCloudModel = it
            countDownLatch.countDown()
        }, {
            throw it
        })
        countDownLatch.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertEquals(expectedCloudModel, actualCloudModel)
    }

    @Test
    fun testConfigServiceIncorrectAnswer() {
        val configHttpService = ConfigHttpService(
            url = configUrl,
            unmarshallResponse = UnmarshallConfigResponse(),
            client = mockOkHttpClient(
                configUrl,
                "{\"incorrect\":{\"isChatEnabled\":true,\"isCallEnabled\":true," +
                        "\"workHours\":\"M-F 9:00 - 18:00\"}}"
            )
        )
        val countDownLatch = CountDownLatch(1)
        var actualException: Throwable? = RuntimeException()
        configHttpService.get({
            countDownLatch.countDown()
        }, {
            actualException = it
            countDownLatch.countDown()
        })
        countDownLatch.await(2000, TimeUnit.MILLISECONDS)

        Assert.assertTrue(actualException is JSONException)
    }

    @Test
    fun testConfigServiceBadAnswer404() {
        internalTestConfigServiceBadAnswer(404)
    }

    @Test
    fun testConfigServiceBadAnswer500() {
        internalTestConfigServiceBadAnswer(500)
    }

    private fun internalTestConfigServiceBadAnswer(code: Int) {
        val configHttpService = ConfigHttpService(
            url = configUrl,
            unmarshallResponse = UnmarshallConfigResponse(),
            client = mockOkHttpClient(configUrl, code)
        )

        val countDownLatch = CountDownLatch(1)
        var occurredException: Throwable = RuntimeException()
        configHttpService.get({
            countDownLatch.countDown()
        }, {
            occurredException = it
            countDownLatch.countDown()
        })
        countDownLatch.await(2000, TimeUnit.MILLISECONDS)

        val actualException = occurredException
        Assert.assertTrue(actualException is BadHttpServiceCall && actualException.code == code)
    }
}