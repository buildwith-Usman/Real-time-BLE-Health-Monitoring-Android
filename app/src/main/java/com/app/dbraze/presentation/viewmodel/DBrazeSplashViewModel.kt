package com.app.dbraze.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.dbraze.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DBrazeSplashViewModel @Inject constructor(): BaseViewModel(){

    private val _navigateToMainActivity = MutableLiveData<Boolean>()
    val navigateToMainActivity: LiveData<Boolean>  = _navigateToMainActivity

    init {
        viewModelScope.launch {
            // Simulate some initial loading or checking
            delay(2000) // Delay for 2 seconds
            _navigateToMainActivity.value = true // Trigger navigation
        }
    }

}
