package com.vetclinic.app.common.ui

class PresenterStore {
    private val presenters = HashMap<String, Presenter>()

    fun get(key: String): Presenter? {
        return presenters[key]
    }

    fun add(key: String, presenter: Presenter) {
        presenters[key] = presenter
    }

    fun remove(key: String): Presenter? {
        return presenters.remove(key)
    }
}