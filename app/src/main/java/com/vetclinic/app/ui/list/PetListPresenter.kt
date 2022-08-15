package com.vetclinic.app.ui.list

import com.vetclinic.app.R
import com.vetclinic.app.common.observer.SingleEventLiveData
import com.vetclinic.app.common.observer.SingleStateLiveData
import com.vetclinic.app.common.ui.Presenter
import com.vetclinic.app.common.ui.UiExecutor
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.workinghours.CheckWorkHours
import com.vetclinic.app.navigation.Navigation
import com.vetclinic.app.navigation.Screen
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class PetListPresenter(
    private val navigation: Navigation.Route,
    private val checkWorkHours: CheckWorkHours,
    private val fetchConfigUseCase: UseCase<ConfigDomain>,
    private val fetchPetsUseCase: UseCase<List<PetDomain>>,
    uiExecutor: UiExecutor = UiExecutor.Main()
) : Presenter {

    private val _configState = SingleStateLiveData(ConfigDomain.EMPTY, uiExecutor)
    val configState get() = _configState.asDataObserver()

    private val _listState = SingleStateLiveData(emptyList<PetDomain>(), uiExecutor)
    val listState get() = _listState.asDataObserver()

    private val _showAlert = SingleEventLiveData<PetListAlert>(uiExecutor)
    val showAlert get() = _showAlert.asDataObserver()

    private val _loadingState = SingleStateLiveData(false, uiExecutor)
    val loadingState get() = _loadingState.asDataObserver()

    private val _errorState = SingleStateLiveData(false, uiExecutor)
    val errorState get() = _errorState.asDataObserver()

    private val _errors = SingleEventLiveData<Throwable>(uiExecutor)
    val errors get() = _errors.asDataObserver()

    private val loadedSources = AtomicInteger(0)
    private val isAnySourceFailed = AtomicBoolean(false)

    init {
        fetch()
    }

    private fun fetch() {
        _loadingState.emit(true)

        // TODO: maybe return Future<R> from use cases
        //  to combine them and handle errors

        fetchConfigUseCase.invoke({
            _configState.emit(it)
            onSourceLoaded()
        }, {
            _errors.emit(it)
            _configState.emit(ConfigDomain.EMPTY)
            onAnySourceFailed()
        })

        fetchPetsUseCase.invoke({
            _listState.emit(it)
            onSourceLoaded()
        }, {
            _errors.emit(it)
            _listState.emit(emptyList())
            onAnySourceFailed()
        })
    }

    private fun onSourceLoaded() {
        if (loadedSources.incrementAndGet() >= TARGET_LOADED_SOURCES) {
            _loadingState.emit(false)
            loadedSources.set(0)
        }
    }

    private fun onAnySourceFailed() {
        if (isAnySourceFailed.compareAndSet(false, true)) {
            _errorState.emit(true)
            _loadingState.emit(false)
            loadedSources.set(0)
        }
    }

    fun retry() {
        isAnySourceFailed.set(false)
        _errorState.emit(false)
        fetch()
    }

    fun routeToPet(petUri: String, petTitle: String) {
        navigation.to(Screen.Pet(petUri, petTitle))
    }

    fun chat() = checkHours()

    fun call() = checkHours()

    private fun checkHours() {
        val isWorking = checkWorkHours.check(_configState.value.workingHours)
        _showAlert.emit(
            PetListAlert(
                R.string.alert_title,
                if (isWorking)
                    R.string.alert_working_hours
                else
                    R.string.alert_not_working_hours
            )
        )
    }

    companion object {
        private const val TARGET_LOADED_SOURCES = 2
    }
}