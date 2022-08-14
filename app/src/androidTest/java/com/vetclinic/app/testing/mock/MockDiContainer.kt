package com.vetclinic.app.testing.mock

import android.widget.ImageView
import com.vetclinic.app.common.fetchimage.FetchImage
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.di.DiContainer
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.WorkingHoursDomain
import com.vetclinic.app.domain.workinghours.CheckWorkingHours
import com.vetclinic.app.navigation.Navigation
import com.vetclinic.app.testing.idling.EspressoIdlingResource

object MockDiContainer : DiContainer {

    var config = DiContainerConfig()

    override val navigation: Navigation.Component = Navigation.Base()

    override val fetchConfigUseCase: UseCase<ConfigDomain> = object : UseCase<ConfigDomain> {
        override fun invoke(onResult: (ConfigDomain) -> Unit, onError: (Throwable) -> Unit) {
            if (!config.isConfigError) {
                onResult.invoke(config.mockedConfig)
            } else {
                onError.invoke(MockedException())
            }
            EspressoIdlingResource.safeDecrement()
        }
    }

    override val fetchPetsUseCase: UseCase<List<PetDomain>> = object : UseCase<List<PetDomain>> {
        override fun invoke(onResult: (List<PetDomain>) -> Unit, onError: (Throwable) -> Unit) {
            if (!config.isPetListError) {
                onResult.invoke(config.mockedPetList)
            } else {
                onError.invoke(MockedException())
            }
            EspressoIdlingResource.safeDecrement()
        }
    }

    override val checkWorkingHours: CheckWorkingHours = object : CheckWorkingHours {
        override fun check(workingHours: WorkingHoursDomain): Boolean {
            return config.isWorkingHours
        }
    }

    override val fetchImage: FetchImage = object : FetchImage {
        override fun into(view: ImageView, url: String, onError: (e: Throwable) -> Unit) = Unit
        override fun cancel(view: ImageView) = Unit
    }
}