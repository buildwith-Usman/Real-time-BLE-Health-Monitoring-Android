package com.app.dbraze.data.repository

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord
import java.util.UUID

interface BluetoothManager {
    fun enableBluetooth() : Boolean

    fun startBLEScan(uuid:String , onScanResult: (List<BluetoothDevice>) -> Unit)

    fun stopBLEScan()
}

