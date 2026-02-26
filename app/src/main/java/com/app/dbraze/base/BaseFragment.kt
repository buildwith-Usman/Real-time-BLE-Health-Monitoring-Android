package com.app.dbraze.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding



abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    protected lateinit var binding: VB


    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateBinding(inflater, container) // Inflate binding in a specific way
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupViews(binding.root)
    }

    // Abstract method for inflating view binding
    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    // Observing common ViewModel properties
    open fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showError(it)
                viewModel.clearError() // Clear error after showing it
            }
        }
    }

    open fun setupViews(view: View) {}

    open fun showLoading() {
        // Override in the fragment to show loading indicator
    }

    open fun hideLoading() {
        // Override in the fragment to hide loading indicator
    }

    open fun showError(message: String) {
        // Override in fragment to show error messages
    }
}
