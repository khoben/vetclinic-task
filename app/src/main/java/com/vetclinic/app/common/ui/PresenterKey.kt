package com.vetclinic.app.common.ui

interface PresenterKey {
    fun presenterKey(): String {
        return this::class.java.canonicalName!!
    }
}