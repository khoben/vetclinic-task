package com.vetclinic.app.ui.pet

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.vetclinic.app.databinding.PetFragmentLayoutBinding
import com.vetclinic.app.ui.base.BaseFragment

class PetFragment : BaseFragment<PetFragmentLayoutBinding, PetPresenter>() {

    override fun presenterFactory(): PetPresenter = PetPresenter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = PetFragmentLayoutBinding.inflate(inflater, container, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = requireArguments().getString(EXTRA_PET_TITLE)
        }

        val uri: String = requireArguments().getString(EXTRA_PET_URI)!!
        with(binding.webView) {
            webViewClient = WebViewClient()
            setBackgroundColor(Color.argb(1, 0, 0, 0))
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            if (savedInstanceState == null) {
                loadUrl(uri)
            } else {
                restoreState(savedInstanceState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }


    companion object {
        val TAG: String = PetFragment::class.java.simpleName

        private const val EXTRA_PET_URI: String = "EXTRA_PET_URI"
        private const val EXTRA_PET_TITLE: String = "EXTRA_PET_TITLE"

        fun create(petUri: String, petTitle: String): PetFragment {
            return PetFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PET_URI, petUri)
                    putString(EXTRA_PET_TITLE, petTitle)
                }
            }
        }
    }
}