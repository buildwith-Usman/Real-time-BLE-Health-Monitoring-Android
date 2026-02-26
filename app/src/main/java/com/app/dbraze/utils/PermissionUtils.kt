package com.app.dbraze.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    // Check if a specific permission is granted
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    // Check if all necessary Bluetooth permissions are granted
    fun hasBluetoothPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return isPermissionGranted(context, Manifest.permission.BLUETOOTH_CONNECT) &&
                    isPermissionGranted(context, Manifest.permission.BLUETOOTH_SCAN)
        }
        return true
    }

    // Request permissions
    fun requestBluetoothPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                ),
                requestCode
            )
        }
    }

    fun hasLocationPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    isPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return true
    }

    // Request permissions
    fun requestLocationPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
        }
    }
}
