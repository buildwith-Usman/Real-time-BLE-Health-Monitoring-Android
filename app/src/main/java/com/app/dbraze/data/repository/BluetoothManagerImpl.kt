package com.app.dbraze.data.repository

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import androidx.annotation.RequiresPermission

class BluetoothManagerImpl(
    private val bluetoothAdapter: BluetoothAdapter, private val context: Context
) : BluetoothManager {

    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private var scanning = false
    private val handler = android.os.Handler()

    private val SCAN_PERIOD: Long = 10000

    private lateinit var leScanCallback: ScanCallback

    init {

    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun enableBluetooth(): Boolean {
        return !bluetoothAdapter.isEnabled
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun startBLEScan(uuid: String, onScanResult: (List<BluetoothDevice>) -> Unit) {
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        val serviceUUID = ParcelUuid.fromString(uuid)
        val scanFilter = ScanFilter.Builder().setServiceUuid(serviceUUID).build()
        val filters: List<ScanFilter> = listOf(scanFilter)
        val scanSettings =
            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

        val scanDevices: MutableList<BluetoothDevice> = mutableListOf()

        val leScanCallback = object : ScanCallback() {
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)

                val scanDevice = result.device
                val deviceName = scanDevice?.name ?: "Unknown Device"

                if (!scanDevices.any { it.name == deviceName }) {
                    if (scanDevice != null) {
                        scanDevices.add(scanDevice)
                    }
                }
            }
        }

        if (!scanning) {
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
                onScanResult(scanDevices)
            }, SCAN_PERIOD)

            scanning = true
            bluetoothLeScanner.startScan(filters, scanSettings, leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun stopBLEScan() {
        bluetoothLeScanner.stopScan(leScanCallback)
    }

}


