package com.app.dbraze.presentation.viewmodel

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.dbraze.base.BaseViewModel
import com.app.dbraze.data.repository.BluetoothManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothManager: BluetoothManager, @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _bluetoothEnabled = MutableLiveData<Boolean>()
    val bluetoothEnabled: LiveData<Boolean> = _bluetoothEnabled

    private val _connectionStatus = MutableLiveData<Boolean>()
    val connectionStatus: LiveData<Boolean>  = _connectionStatus

    private val _scanResults = MutableLiveData<List<BluetoothDevice>>()
    val scanResults: LiveData<List<BluetoothDevice>> = _scanResults

    fun enableBluetooth() {
        _bluetoothEnabled.value = bluetoothManager.enableBluetooth()
    }

    fun scanBLE(uuid:String) {
        bluetoothManager.startBLEScan(uuid) { results ->
            if(results.isNotEmpty()){
                _scanResults.value = results
            }
        }
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        _connectionStatus.postValue(isConnected)
    }

}
