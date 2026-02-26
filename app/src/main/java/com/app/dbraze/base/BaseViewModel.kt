package com.app.dbraze.base


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    // Common loading state LiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Common error message LiveData
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Method to show the loading state
    protected fun showLoading() {
        _isLoading.value = true
    }

    // Method to hide the loading state
    protected fun hideLoading() {
        _isLoading.value = false
    }

    // Method to handle errors and set error message
    protected fun setError(message: String?) {
        _errorMessage.value = message
    }

    // Method to clear error message
    fun clearError() {
        _errorMessage.value = null
    }
}


