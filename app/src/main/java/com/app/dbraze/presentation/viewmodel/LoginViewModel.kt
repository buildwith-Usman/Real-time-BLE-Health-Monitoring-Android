package com.app.dbraze.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.dbraze.base.BaseViewModel
import com.app.dbraze.data.repository.AuthRepository
import com.app.dbraze.data.repository.UserRepository
import com.app.dbraze.utils.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val validationErrors = MutableLiveData<Map<String, String>>()

    private val _isValidLoginForm = MutableLiveData<Boolean>()
    val isValidLoginForm: LiveData<Boolean> get() = _isValidLoginForm

    private val _loginResult = MutableLiveData<Result<Boolean>>()
    val loginResult: LiveData<Result<Boolean>> get() = _loginResult

    fun validateForm(email: String?, password: String?) {
        val errors = mutableMapOf<String, String>()

        if (!InputValidator.isValidEmail(email)) {
            errors["email"] = "Invalid email format"
        } else {
            errors.remove("email")
        }


        if (!InputValidator.isValidPassword(password)) {
            errors["password"] = "Password must be at least 8 characters"
        } else {
            errors.remove("password") // Clear error if validation succeeds
        }

        validationErrors.value = errors
        _isValidLoginForm.value = errors.isEmpty()
    }

    fun loginUser(userData: Map<String, Any>) {
        viewModelScope.launch {
            try {
                showLoading()
                val authResult = authRepository.loginUser(
                    userData["email"].toString(),
                    userData["password"].toString()
                ).await()
                if (authResult.user != null) {
                    _loginResult.value = Result.success(true)
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
                setError(e.message)
            }
            finally {
                hideLoading()
            }
        }
    }
}