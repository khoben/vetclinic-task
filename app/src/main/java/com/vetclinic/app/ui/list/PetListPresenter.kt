package com.vetclinic.app.ui.list

import android.util.Log
import com.vetclinic.app.R
import com.vetclinic.app.common.observer.SingleEventLiveData
import com.vetclinic.app.common.observer.SingleStateLiveData
import com.vetclinic.app.common.ui.Presenter
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.workinghours.CheckWorkingHours
import com.vetclinic.app.navigation.Navigation
import com.vetclinic.app.navigation.Screen

class PetListPresenter(
    private val navigation: Navigation.Route,
    private val fetchConfigUseCase: UseCase<ConfigDomain>,
    private val fetchPetsUseCase: UseCase<List<PetDomain>>,
    private val checkWorkingHours: CheckWorkingHours
) : Presenter {

    private val _configObserver = SingleStateLiveData(ConfigDomain.EMPTY)
    val configObserver get() = _configObserver.asDataObserver()

    private val _listObserver = SingleStateLiveData(emptyList<PetDomain>())
    val listObserver get() = _listObserver.asDataObserver()

    private val _showAlert = SingleEventLiveData<PetListAlert>()
    val showAlert get() = _showAlert.asDataObserver()

    private val _errors = SingleEventLiveData<Throwable>()
    val errors get() = _errors.asDataObserver()

    private var alreadyFetched = false

    /**
     * Since our "LiveData" irrespective to fragment lifecycle
     * we should call [fetchData] every time @ onViewCreated() or smth
     * and check if it has been fetched already
     */
    fun fetchData() {
        if (alreadyFetched) return
        alreadyFetched = true

        fetchConfigUseCase.invoke({
            _configObserver.emit(it)
        }, {
            _errors.emit(it)
        })

        fetchPetsUseCase.invoke({
            _listObserver.emit(it)
        }, {
            _errors.emit(it)
        })
    }

    fun routeToPet(petUri: String, petTitle: String) {
        navigation.to(Screen.Pet(petUri, petTitle))
    }

    fun chat() = checkHours { }

    fun call() = checkHours { }

    private fun checkHours(doOnWorkingHours: () -> Unit) {
        val isWorking = checkWorkingHours.check(_configObserver.value.workingHours)
        if (isWorking) {
            doOnWorkingHours()
        }

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
}