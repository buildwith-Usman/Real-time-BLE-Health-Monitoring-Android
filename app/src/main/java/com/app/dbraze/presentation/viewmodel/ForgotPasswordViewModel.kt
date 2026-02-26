package com.app.dbraze.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.dbraze.base.BaseViewModel
import com.app.dbraze.data.repository.AuthRepository
import com.app.dbraze.utils.InputValidator
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    val validationErrors = MutableLiveData<Map<String, String>>()

    private val _isValidEmail = MutableLiveData<Boolean>()
    val isValidEmail: LiveData<Boolean> get() = _isValidEmail

    private val _resetPasswordEmail = MutableLiveData<Result<Boolean>>()
    val resetPasswordEmail: LiveData<Result<Boolean>> get() = _resetPasswordEmail

    fun validateEmail(email: String?) {
        val errors = mutableMapOf<String, String>()

        if (!InputValidator.isValidEmail(email)) {
            errors["email"] = "Invalid email format"
        }else {
            errors.remove("email")
        }

        validationErrors.value = errors
        _isValidEmail.value = errors.isEmpty()
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            showLoading()

            val task: Task<Void> = authRepository.resetPassword(email)

            task.addOnCompleteListener { taskResult ->
                hideLoading()
                if (taskResult.isSuccessful) {
                    _resetPasswordEmail.value = Result.success(true)
                } else {
                    _resetPasswordEmail.value = Result.success(false)
                    setError(task.exception.toString())
                }
            }
        }
    }

}