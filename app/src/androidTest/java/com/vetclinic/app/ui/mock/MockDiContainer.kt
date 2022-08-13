package com.vetclinic.app.ui.mock

import android.widget.ImageView
import com.vetclinic.app.common.fetchimage.FetchImage
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.di.DiContainer
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import com.vetclinic.app.domain.workinghours.CheckWorkingHours
import com.vetclinic.app.navigation.Navigation
import com.vetclinic.app.ui.idling.EspressoIdlingResource

object MockDiContainer : DiContainer {

    var mockedConfig = ConfigDomain.EMPTY
    var mockedPetList = emptyList<PetDomain>()

    var mockedConfigError = false
    var mockedPetListError = false

    var mockedWorkingHours = true

    override val navigation: Navigation.Component = Navigation.Base()

    override val fetchConfigUseCase: UseCase<ConfigDomain> = object : UseCase<ConfigDomain> {
        override fun invoke(onResult: (ConfigDomain) -> Unit, onError: (Throwable) -> Unit) {
            if (!mockedConfigError) {
                onResult.invoke(mockedConfig)
            } else {
                onError.invoke(MockedException())
            }
            EspressoIdlingResource.safeDecrement()
        }
    }

    override val fetchPetsUseCase: UseCase<List<PetDomain>> = object : UseCase<List<PetDomain>> {
        override fun invoke(onResult: (List<PetDomain>) -> Unit, onError: (Throwable) -> Unit) {
            if (!mockedPetListError) {
                onResult.invoke(mockedPetList)
            } else {
                onError.invoke(MockedException())
            }
            EspressoIdlingResource.safeDecrement()
        }
    }

    override val checkWorkingHours: CheckWorkingHours = object : CheckWorkingHours {
        override fun check(workingHours: WorkingHoursDomain): Boolean {
            return mockedWorkingHours
        }
    }

    override val fetchImage: FetchImage = object : FetchImage {
        override fun into(view: ImageView, url: String, onError: (e: Throwable) -> Unit) = Unit
        override fun cancel(view: ImageView) = Unit
    }
}