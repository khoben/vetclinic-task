package com.vetclinic.app.common.ui

class WithLifecycleCollector {
    private val children = mutableListOf<WithLifecycle>()

    operator fun WithLifecycle.unaryPlus(): WithLifecycle {
        this@WithLifecycleCollector.children.add(this)
        return this
    }

    fun build() = children
}