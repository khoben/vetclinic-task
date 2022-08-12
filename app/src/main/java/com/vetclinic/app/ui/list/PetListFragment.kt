package com.vetclinic.app.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.vetclinic.app.R
import com.vetclinic.app.common.extensions.isVisible
import com.vetclinic.app.databinding.PetListFragmentLayoutBinding
import com.vetclinic.app.di.di
import com.vetclinic.app.domain.usecase.FetchConfigUseCase
import com.vetclinic.app.domain.usecase.FetchPetsUseCase
import com.vetclinic.app.domain.workinghours.CheckWorkingHours
import com.vetclinic.app.domain.workinghours.CurrentHour
import com.vetclinic.app.domain.workinghours.ParseWorkingHours
import com.vetclinic.app.ui.base.BaseFragment


class PetListFragment : BaseFragment<PetListFragmentLayoutBinding, PetListPresenter>() {

    private val petListPresenter by lazy(LazyThreadSafetyMode.NONE) { obtainPresenter() }

    override fun presenterFactory(): PetListPresenter = PetListPresenter(
        di.navigation,
        FetchConfigUseCase(di.configHttpService, ConfigMapper(ParseWorkingHours.Base())),
        FetchPetsUseCase(di.petHttpService, PetListMapper()),
        CheckWorkingHours.Base(CurrentHour.Base())
    )

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = PetListFragmentLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.app_name)
        }

        val petAdapter = PetListAdapter(di.fetchImage) { pet ->
            petListPresenter.routeToPet(pet.contentUrl, pet.title)
        }.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        with(binding.petList) {
            adapter = petAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.chatBtn.setOnClickListener { petListPresenter.chat() }
        binding.callBtn.setOnClickListener { petListPresenter.call() }

        petListPresenter.configObserver.observe {
            binding.buttonSpacer.isVisible = it.isCallEnabled && it.isChatEnabled
            binding.callBtn.isVisible = it.isCallEnabled
            binding.chatBtn.isVisible = it.isChatEnabled
            binding.workingHours.isVisible = it.workingHours.origin.isNotEmpty()
            binding.workingHours.text = getString(R.string.office_hours, it.workingHours.origin)
        }
        petListPresenter.listObserver.observe { petAdapter.submitList(it) }
        petListPresenter.showAlert.observe { showAlert(it.title, it.message) }
        petListPresenter.errors.observe {
            showToast(getString(R.string.generic_error, it.message))
            binding.errorsLayout.isVisible = true
        }

        petListPresenter.fetchData()
    }

    companion object {
        val TAG: String = PetListFragment::class.java.simpleName
    }
}