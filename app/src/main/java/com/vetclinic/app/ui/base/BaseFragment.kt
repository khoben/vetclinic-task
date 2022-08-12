package com.vetclinic.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.vetclinic.app.R
import com.vetclinic.app.common.ui.Presenter
import com.vetclinic.app.common.ui.PresenterFragment

abstract class BaseFragment<B : ViewBinding, P : Presenter> : PresenterFragment<P>() {
    private var _binding: B? = null
    protected val binding: B get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return _binding?.root
    }

    fun showAlert(@StringRes title: Int, @StringRes message: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun showToast(@StringRes messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), getString(messageResId), duration).show()
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}