package com.vetclinic.app.di

import com.vetclinic.app.common.fetchimage.FetchImage
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.workinghours.CheckWorkingHours
import com.vetclinic.app.navigation.Navigation

interface DiContainer {
    val navigation: Navigation.Component
    val fetchConfigUseCase: UseCase<ConfigDomain>
    val fetchPetsUseCase: UseCase<List<PetDomain>>
    val checkWorkingHours: CheckWorkingHours
    val fetchImage: FetchImage
}