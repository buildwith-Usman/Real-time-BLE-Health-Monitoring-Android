package com.app.dbraze.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.dbraze.base.BaseViewModel
import com.app.dbraze.data.repository.AuthRepository
import com.app.dbraze.data.repository.UserRepository
import com.app.dbraze.utils.InputValidator
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val validationErrors = MutableLiveData<Map<String, String>>()

    private val _navigateToPasswordView = MutableLiveData<Boolean>()
    val navigateToPasswordView: LiveData<Boolean> get() = _navigateToPasswordView

    private val _isValidPasswordForm = MutableLiveData<Boolean>()
    val isValidPasswordForm: LiveData<Boolean> get() = _isValidPasswordForm

    private val _signUpResult = MutableLiveData<Result<Boolean>>()
    val signUpResult: LiveData<Result<Boolean>> get() = _signUpResult


    fun validatePersonalInformationForm(email: String?) {
        val errors = mutableMapOf<String, String>()


        if (!InputValidator.isValidEmail(email)) {
            errors["email"] = "Invalid email format"
        }else {
            errors.remove("email")
        }

        validationErrors.value = errors
        if (errors.isEmpty()) {
            _navigateToPasswordView.value = true
        }
    }

    fun validatePasswordForm(password: String?, confirmPassword: String?) {
        val errors = mutableMapOf<String, String>()

        if (password.isNullOrBlank()) {
            errors["password"] = "Password is required."
        } else if (!InputValidator.isValidPassword(password)) {
            errors["password"] = "Enter a correct password (minimum 8 characters, at least one letter and one number)."
        } else {
            errors.remove("password") // Remove any existing password error
        }

        if (confirmPassword.isNullOrBlank()) {
            errors["confirmPassword"] = "Confirm password is required."
        } else if (confirmPassword != password) {
            errors["confirmPassword"] = "Passwords do not match."
        } else {
            errors.remove("confirmPassword") // Remove any existing confirm password error
        }

        validationErrors.value = errors
        _isValidPasswordForm.value = errors.isEmpty()
    }

    fun createUser(userData: Map<String, Any>) {
        viewModelScope.launch {
            try {
                showLoading()
                val authResult = authRepository.createUser(userData["email"].toString(), userData["password"].toString()).await()
                val userId = authResult.user?.uid
                if (userId != null) {
                    userRepository.saveUserDataToFireStore(userId, userData).await()
                    _signUpResult.value = Result.success(true)
                }
            } catch (e: Exception) {
                _signUpResult.value = Result.failure(e)
                setError(e.message)
            }
            finally {
                hideLoading()
            }
        }
    }

}