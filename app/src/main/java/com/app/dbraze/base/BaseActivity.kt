package com.app.dbraze.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint



abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    protected abstract val viewModel: VM

    // Declare a variable for View Binding
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding()  // Inflate View Binding
        setContentView(binding.root) // Set content view using binding
        observeViewModel() // Observe common ViewModel events
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    // Child classes will specify their own ViewModel class
    abstract fun getViewModelClass(): Class<VM>

    // Inflate View Binding in child classes
    abstract fun inflateBinding(): VB

    // Observe common ViewModel behaviors
    open fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            handleLoadingState(isLoading)
        }
    }

    // Centralized loading state handling
    private fun handleLoadingState(isLoading: Boolean) {
        if (isLoading) showLoading() else hideLoading()
    }

    open fun showLoading() {
        // Child classes can override this to show loading indicator
    }

    open fun hideLoading() {
        // Child classes can override this to hide loading indicator
    }

    // Optionally handle error state if you have error LiveData in ViewModel
    open fun handleError(error: String) {
        // Handle error appropriately in child classes
    }
}
