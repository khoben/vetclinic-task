package com.vetclinic.app.common.ui

import android.content.Context
import androidx.fragment.app.Fragment

abstract class PresenterFragment<P : Presenter> : Fragment(), PresenterKey {

    private var presenterStore: PresenterStore? = null

    protected abstract fun presenterFactory(): P

    @Suppress("UNCHECKED_CAST")
    protected fun obtainPresenter(): P {
        val store = presenterStore
        require(store != null)
        val presenter =
            store.get(presenterKey()) ?: presenterFactory().also { store.add(presenterKey(), it) }
        return presenter as P
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PresenterActivity<*>) {
            presenterStore = context.getPresenterStore()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!requireActivity().isChangingConfigurations) {
            presenterStore?.let { store ->
                store.get(presenterKey())?.onDestroy()
                store.remove(presenterKey())
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        presenterStore = null
    }

}