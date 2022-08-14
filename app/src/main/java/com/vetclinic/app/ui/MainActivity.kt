package com.vetclinic.app.ui

import android.os.Bundle
import com.vetclinic.app.R
import com.vetclinic.app.common.ui.PresenterActivity
import com.vetclinic.app.di.di

class MainActivity : PresenterActivity<MainPresenter>() {

    private val presenter by lazy(LazyThreadSafetyMode.NONE) { obtainPresenter() }

    override fun presenterFactory(): MainPresenter = MainPresenter(di.navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        destroyable(
            presenter.navigationObserver.observe { screen ->
                screen.show(R.id.container, supportFragmentManager)
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return false
    }
}