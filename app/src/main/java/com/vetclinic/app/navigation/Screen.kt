package com.vetclinic.app.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vetclinic.app.ui.pet.PetFragment

sealed class Screen(
    private val id: String,
    private val factory: () -> Fragment,
    private val mode: Mode
) {
    fun show(containerId: Int, fragmentManager: FragmentManager) =
        mode.show(id, factory, containerId, fragmentManager)

    class Pet(petUri: String, petTitle: String) : Screen(
        id = PetFragment.TAG,
        factory = { PetFragment.create(petUri, petTitle) },
        mode = Mode.Replace
    )

    object Test : Screen("Test", {Fragment()}, Mode.Root)
}