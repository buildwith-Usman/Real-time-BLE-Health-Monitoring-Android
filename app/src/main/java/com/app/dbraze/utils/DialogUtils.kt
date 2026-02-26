package com.app.dbraze.utils


import android.content.Context


object DialogUtils {

    fun showBluetoothConnectedDialog(
        context: Context,
        deviceName: String? = null,
        onConnectedDoneClick: () -> Unit = {},
    ) {
        val dialog = ShowBluetoothConnectedDialog(
            context,
            onConnectedDoneClick = onConnectedDoneClick,
            deviceName = deviceName
        )
        dialog.show()
    }


}
