package com.vetclinic.app.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Mode {

    fun show(
        id: String,
        factory: () -> Fragment,
        containerId: Int,
        fragmentManager: FragmentManager
    )

    object Root : Mode {
        override fun show(
            id: String,
            factory: () -> Fragment,
            containerId: Int,
            fragmentManager: FragmentManager
        ) {
            fragmentManager.beginTransaction()
                .add(containerId, factory(), id)
                .commit()
        }
    }

    object Add : Mode {
        override fun show(
            id: String,
            factory: () -> Fragment,
            containerId: Int,
            fragmentManager: FragmentManager
        ) {
            fragmentManager.beginTransaction()
                .add(containerId, factory(), id)
                .addToBackStack(null)
                .commit()
        }
    }

    object Replace : Mode {
        override fun show(
            id: String,
            factory: () -> Fragment,
            containerId: Int,
            fragmentManager: FragmentManager
        ) {
            fragmentManager.beginTransaction()
                .replace(containerId, factory(), id)
                .addToBackStack(null)
                .commit()
        }
    }
}