package com.vetclinic.app.ui.list

import com.vetclinic.app.common.observer.SingleEventLiveData
import com.vetclinic.app.common.observer.SingleStateLiveData
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.HourDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import com.vetclinic.app.domain.workinghours.CheckWorkingHours
import com.vetclinic.app.domain.workinghours.CurrentHour
import com.vetclinic.app.navigation.Navigation
import org.junit.Assert
import org.junit.Test

class PetListPresenterTest {

    private val navigation = Navigation.Base()
    private val expectedConfigDomain = ConfigDomain(
        true, true, WorkingHoursDomain(
            "M-F 9:00 - 18:00",
            HourDomain(2, 9, 0),
            HourDomain(6, 18, 0)
        )
    )
    private val expectedPetDomain = listOf(
        PetDomain(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
            "Cat",
            "https://en.wikipedia.org/wiki/Cat"
        )
    )

    @Test
    fun testFetchData() {
        val presenter = PetListPresenter(
            navigation,
            object : UseCase<ConfigDomain> {
                override fun invoke(
                    onResult: (ConfigDomain) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedConfigDomain)
                }
            },
            object : UseCase<List<PetDomain>> {
                override fun invoke(
                    onResult: (List<PetDomain>) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedPetDomain)
                }
            },
            CheckWorkingHours.Base(object : CurrentHour {
                override fun get(): HourDomain {
                    return HourDomain(2, 15, 0)
                }
            })
        )
        presenter.fetchData()
        Assert.assertEquals(
            expectedPetDomain,
            (presenter.listObserver as SingleStateLiveData).value
        )
        Assert.assertEquals(
            expectedConfigDomain,
            (presenter.configObserver as SingleStateLiveData).value
        )
    }

    @Test
    fun testFetchDataError() {
        val presenter = PetListPresenter(
            navigation,
            object : UseCase<ConfigDomain> {
                override fun invoke(
                    onResult: (ConfigDomain) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onError.invoke(RuntimeException())
                }
            },
            object : UseCase<List<PetDomain>> {
                override fun invoke(
                    onResult: (List<PetDomain>) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onError.invoke(RuntimeException())
                }
            },
            CheckWorkingHours.Base(object : CurrentHour {
                override fun get(): HourDomain {
                    return HourDomain(2, 15, 0)
                }
            })
        )

        presenter.fetchData()

        Assert.assertEquals(
            emptyList<PetDomain>(),
            (presenter.listObserver as SingleStateLiveData).value
        )
        Assert.assertEquals(
            ConfigDomain.EMPTY,
            (presenter.configObserver as SingleStateLiveData).value
        )

        Assert.assertTrue((presenter.errors as SingleEventLiveData).lastEmittedData is java.lang.RuntimeException)
    }

    @Test
    fun testFetchDataCalling() {
        val presenter = PetListPresenter(
            navigation,
            object : UseCase<ConfigDomain> {
                override fun invoke(
                    onResult: (ConfigDomain) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedConfigDomain)
                }
            },
            object : UseCase<List<PetDomain>> {
                override fun invoke(
                    onResult: (List<PetDomain>) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedPetDomain)
                }
            },
            CheckWorkingHours.Base(object : CurrentHour {
                override fun get(): HourDomain {
                    return HourDomain(2, 15, 0)
                }
            })
        )

        presenter.fetchData()
        presenter.call()

        Assert.assertTrue((presenter.showAlert as SingleEventLiveData).lastEmittedData is PetListAlert)

        (presenter.showAlert as SingleEventLiveData).lastEmittedData = null

        presenter.chat()

        Assert.assertTrue((presenter.showAlert as SingleEventLiveData).lastEmittedData is PetListAlert)
    }

    @Test
    fun testCallingWorking() {
        val presenter = PetListPresenter(
            navigation,
            object : UseCase<ConfigDomain> {
                override fun invoke(
                    onResult: (ConfigDomain) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedConfigDomain)
                }
            },
            object : UseCase<List<PetDomain>> {
                override fun invoke(
                    onResult: (List<PetDomain>) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedPetDomain)
                }
            },
            CheckWorkingHours.Base(object : CurrentHour {
                override fun get(): HourDomain {
                    return HourDomain(2, 15, 0)
                }
            })
        )

        presenter.fetchData()
        presenter.call()

        Assert.assertTrue((presenter.showAlert as SingleEventLiveData).lastEmittedData is PetListAlert)
        var alert = (presenter.showAlert as SingleEventLiveData).lastEmittedData as PetListAlert

        Assert.assertTrue(alert.message == com.vetclinic.app.R.string.alert_working_hours)

        (presenter.showAlert as SingleEventLiveData).lastEmittedData = null

        presenter.chat()

        Assert.assertTrue((presenter.showAlert as SingleEventLiveData).lastEmittedData is PetListAlert)
        alert = (presenter.showAlert as SingleEventLiveData).lastEmittedData as PetListAlert

        Assert.assertTrue(alert.message == com.vetclinic.app.R.string.alert_working_hours)
    }

    @Test
    fun testCallingNotWorking() {
        val presenter = PetListPresenter(
            navigation,
            object : UseCase<ConfigDomain> {
                override fun invoke(
                    onResult: (ConfigDomain) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedConfigDomain)
                }
            },
            object : UseCase<List<PetDomain>> {
                override fun invoke(
                    onResult: (List<PetDomain>) -> Unit,
                    onError: (Throwable) -> Unit
                ) {
                    onResult.invoke(expectedPetDomain)
                }
            },
            CheckWorkingHours.Base(object : CurrentHour {
                override fun get(): HourDomain {
                    return HourDomain(1, 15, 0)
                }
            })
        )

        presenter.fetchData()
        presenter.call()

        Assert.assertTrue((presenter.showAlert as SingleEventLiveData).lastEmittedData is PetListAlert)
        var alert = (presenter.showAlert as SingleEventLiveData).lastEmittedData as PetListAlert

        Assert.assertTrue(alert.message == com.vetclinic.app.R.string.alert_not_working_hours)

        (presenter.showAlert as SingleEventLiveData).lastEmittedData = null

        presenter.chat()

        Assert.assertTrue((presenter.showAlert as SingleEventLiveData).lastEmittedData is PetListAlert)
        alert = (presenter.showAlert as SingleEventLiveData).lastEmittedData as PetListAlert

        Assert.assertTrue(alert.message == com.vetclinic.app.R.string.alert_not_working_hours)
    }
}