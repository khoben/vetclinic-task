package com.vetclinic.app.common.ui

import androidx.appcompat.app.AppCompatActivity

abstract class PresenterActivity<P : Presenter> : AppCompatActivity(), PresenterKey {

    private var presenterStore: PresenterStore? = null
    private val bagOfDestroyable = mutableListOf<WithLifecycle>()

    protected abstract fun presenterFactory(): P

    protected fun obtainPresenter(): P {
        @Suppress("UNCHECKED_CAST")
        return getPresenterStore().get(presenterKey()) as? P?
            ?: presenterFactory().also { getPresenterStore().add(presenterKey(), it) }
    }

    fun getPresenterStore(): PresenterStore {
        requireNotNull(application)
        val store = presenterStore
        return if (store == null) {
            presenterStore = getPresenterStoreFromNCI()
            if (presenterStore == null) {
                presenterStore = PresenterStore()
            }
            presenterStore!!
        } else {
            store
        }
    }

    /**
     * Should destroy on onDestroy()
     */
    fun withLifecycle(destroyable: WithLifecycle) {
        bagOfDestroyable.add(destroyable)
    }

    /**
     * Should destroy on onDestroyView()
     */
    fun withLifecycle(block: WithLifecycleCollector.() -> Unit) {
        bagOfDestroyable.addAll(WithLifecycleCollector().apply(block).build())
    }

    @Suppress("DEPRECATION")
    private fun getPresenterStoreFromNCI(): PresenterStore? {
        return (lastCustomNonConfigurationInstance as NonConfigurationInstances?)?.presenterStore
    }

    @Deprecated("Deprecated in Java")
    override fun onRetainCustomNonConfigurationInstance(): Any {
        val nci = NonConfigurationInstances()
        nci.presenterStore = presenterStore
        nci.custom = onRetainCustomNonConfigurationObject()
        return nci
    }

    open fun onRetainCustomNonConfigurationObject(): Any? {
        return null
    }

    @Suppress("DEPRECATION")
    fun getLastCustomNonConfigurationObject(): Any? {
        return (lastCustomNonConfigurationInstance as NonConfigurationInstances?)?.custom
    }

    override fun onDestroy() {
        bagOfDestroyable.forEach(WithLifecycle::onDestroy)
        bagOfDestroyable.clear()

        super.onDestroy()
        if (!isChangingConfigurations) {
            presenterStore?.let { store ->
                store.get(presenterKey())?.onDestroy()
                store.remove(presenterKey())
            }
        }
    }

    internal class NonConfigurationInstances {
        var custom: Any? = null
        var presenterStore: PresenterStore? = null
    }
}