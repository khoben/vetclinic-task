package com.vetclinic.app.navigation

import com.vetclinic.app.common.observer.DataObserver
import com.vetclinic.app.common.observer.SingleEventLiveData

interface Navigation {

    interface Route {
        fun to(screen: Screen)
    }

    interface Observe {
        fun observe(): DataObserver<Screen>
    }

    interface Component : Route, Observe

    class Base : Component {

        private val navigationLiveData = SingleEventLiveData<Screen>()

        override fun to(screen: Screen) {
            navigationLiveData.emit(screen)
        }

        override fun observe(): DataObserver<Screen> =
            navigationLiveData.asDataObserver()
    }
}