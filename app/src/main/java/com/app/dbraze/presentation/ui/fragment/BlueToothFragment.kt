package com.app.dbraze.presentation.ui.fragment

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dbraze.R
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.BluetoothDevicesItemBinding
import com.app.dbraze.databinding.BluetoothFragmentBinding
import com.app.dbraze.presentation.viewmodel.BluetoothViewModel
import com.app.dbraze.presentation.views.GenericAdapter
import com.app.dbraze.utils.CircularAnimation
import com.app.dbraze.utils.DialogUtils
import com.app.dbraze.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

@AndroidEntryPoint
class BlueToothFragment : BaseFragment<BluetoothFragmentBinding, BluetoothViewModel>() {

    override val viewModel: BluetoothViewModel by viewModels()
    private lateinit var adapter: GenericAdapter<BluetoothDevice, BluetoothDevicesItemBinding>
    private lateinit var circularAnimation: CircularAnimation
    private lateinit var deviceName: String


    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): BluetoothFragmentBinding {
        return BluetoothFragmentBinding.inflate(inflater, container, false)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun setupViews(view: View) {
        setupAppBar()
        setConnectSpannable()
        setupBlueToothDevicesLayout()
        handleClicks()
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.bluetoothEnabled.observe(viewLifecycleOwner) { isEnabled ->
            if (isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                showDevicesHideBlueToothConnectionLayout()
            }
        }

        viewModel.scanResults.observe(viewLifecycleOwner) { scanResults ->
            adapter.setItems(scanResults)
            circularAnimation.stopAnimation()
        }

        viewModel.connectionStatus.observe(viewLifecycleOwner) {
            if (it) {
                DialogUtils.showBluetoothConnectedDialog(context = requireContext(),
                    deviceName = deviceName,
                    onConnectedDoneClick = {
                        findNavController().navigateUp()
                    })
            }
        }

    }

    override fun showError(message: String) {
        super.showError(message)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun setupBlueToothDevicesLayout() {
        circularAnimation =
            CircularAnimation(requireContext(), binding.bluetoothDevicesLayout.bluetoothSyncIcon)
        setupRecyclerView()
        setupBlueToothDevicesAdapter()
    }

    private fun setupRecyclerView() {
        binding.bluetoothDevicesLayout.blueToothDevicesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun setupBlueToothDevicesAdapter() {

        adapter =
            GenericAdapter(inflate = BluetoothDevicesItemBinding::inflate, bind = { binding, item ->

                item.name?.let {
                    binding.bluetoothDeviceName.text = item.name.toString()
                }

            }, itemClickListener = { item ->
                circularAnimation.stopAnimation()
                deviceName = item.name
                showBlueToothParingLayout()
                item.connectGatt(requireContext(), false, object : BluetoothGattCallback() {
                    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
                    override fun onConnectionStateChange(
                        gatt: BluetoothGatt, status: Int, newState: Int
                    ) {
                        super.onConnectionStateChange(gatt, status, newState)
                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            Log.d(ScreenTag, "onBlueTooth Connected")
                            viewModel.updateConnectionStatus(true)
                            gatt.discoverServices()
                        } else {
                            Log.d(ScreenTag, "onBlueTooth DisConnected")
                            viewModel.updateConnectionStatus(false)
                        }
                    }

                    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
                    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                        super.onServicesDiscovered(gatt, status)
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            val targetService =
                                gatt.getService(UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"))
                            if (targetService != null) {
                                Log.i("BLE", "Target Service UUID found: $SERVICE_UUID")

                                val batteryCharacteristicUUID =
                                    UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E") // Replace with actual characteristic UUID
                                val batteryCharacteristic =
                                    targetService.getCharacteristic(batteryCharacteristicUUID)

                                if (batteryCharacteristic != null) {
                                    if (batteryCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                                        gatt.setCharacteristicNotification(
                                            batteryCharacteristic,
                                            true
                                        )
                                        val descriptor = batteryCharacteristic.getDescriptor(
                                            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
                                        )
                                        descriptor.value =
                                            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                        Log.d(
                                            "BLE",
                                            "Descriptor value set to: ${
                                                descriptor.value.joinToString(", ") { it.toString() }
                                            }"
                                        )
                                        gatt.writeDescriptor(descriptor)
                                    } else {
                                        Log.w(
                                            "BLE",
                                            "Battery characteristic does not support notifications"
                                        )
                                    }
                                } else {
                                    Log.w(
                                        "BLE",
                                        "Battery characteristic with UUID $batteryCharacteristicUUID not found"
                                    )
                                }
                            } else {
                                Log.w(
                                    "BLE",
                                    "Service with UUID $SERVICE_UUID not found among discovered services"
                                )
                            }
                        } else {
                            Log.w("BLE", "onServicesDiscovered received: $status")
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onCharacteristicChanged(
                        gatt: BluetoothGatt,
                        characteristic: BluetoothGattCharacteristic
                    ) {
                        super.onCharacteristicChanged(gatt, characteristic)

                        if (characteristic.uuid == UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")) {

                            val data = characteristic.value
                            val jsonString = data.toString(Charsets.UTF_8)
                            println("Decoded JSON String: $jsonString")

                            try {
                                val jsonObject = JSONObject(jsonString)
                                val led = jsonObject.getInt("led")
                                val charger = jsonObject.getInt("charger")
                                val batVolt = jsonObject.getDouble("bat_volt")

                                println("LED State: $led")
                                println("Charger State: $charger")
                                println("Battery Voltage: $batVolt")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                })

            })

        binding.bluetoothDevicesLayout.blueToothDevicesRecyclerView.adapter = adapter

    }


    private fun setConnectSpannable() {
        val connectText = resources.getString(R.string.connect_text)
        val spannableString = SpannableString(connectText)
        val boldSpan = StyleSpan(Typeface.BOLD)
        val sizeSpan20 = AbsoluteSizeSpan(20, true)
        spannableString.setSpan(boldSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(sizeSpan20, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val sizeSpan14 = AbsoluteSizeSpan(14, true)
        spannableString.setSpan(sizeSpan14, 7, connectText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val modifiedText = spannableString.toString().replace("Connect ", "Connect\n")
        val newSpannableString = SpannableString(modifiedText)

        newSpannableString.setSpan(boldSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        newSpannableString.setSpan(sizeSpan20, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        newSpannableString.setSpan(
            sizeSpan14, 7, modifiedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.bluetoothConnectionLayout.connectText.text = newSpannableString

    }

    private fun setupAppBar() {
        binding.bluetoothAppBar.title.text = resources.getString(R.string.bluetooth)
    }

    private fun handleClicks() {

        binding.bluetoothConnectionLayout.btnTurnOn.setOnClickListener {
            checkPermissionsAndEnableBluetooth()
        }

        binding.bluetoothAppBar.backFromFragment.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bluetoothPairingLayout.btnCancelPairing.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDevicesHideBlueToothConnectionLayout() {
        binding.bluetoothDevicesLayout.devicesMain.visibility = View.VISIBLE
        binding.bluetoothConnectionLayout.connectionMain.visibility = View.GONE
        circularAnimation.startAnimation()

        checkLocationPermissionsAndDiscoverBluetooth()

    }

    private fun showBlueToothParingLayout() {
        binding.bluetoothPairingLayout.pairingMain.visibility = View.VISIBLE
        binding.bluetoothDevicesLayout.devicesMain.visibility = View.GONE
        binding.bluetoothPairingLayout.lottieAnimationView.playAnimation()
        setBlueToothPairingSpannableText()

    }

    private fun setBlueToothPairingSpannableText() {

        val deviceName = deviceName
        val fullText = "Please wait, $deviceName is pairing"

        val spannableStringBuilder = SpannableStringBuilder(fullText)

        val start = fullText.indexOf(deviceName)
        val end = start + deviceName.length

        spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, 0)
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(), R.color.d_braze_primary
                )
            ), start, end, 0
        )

        binding.bluetoothPairingLayout.pairingText.text = spannableStringBuilder
    }


    private fun showConnectionLayoutHideDevicesLayout() {
        binding.bluetoothDevicesLayout.devicesMain.visibility = View.GONE
        binding.bluetoothConnectionLayout.connectionMain.visibility = View.VISIBLE
    }

    private fun checkPermissionsAndEnableBluetooth() {
        if (PermissionUtils.hasBluetoothPermissions(requireContext())) {
            viewModel.enableBluetooth()
        } else {
            PermissionUtils.requestBluetoothPermissions(
                requireActivity(), REQUEST_BLUETOOTH_PERMISSION_CODE
            )
        }
    }

    private fun checkLocationPermissionsAndDiscoverBluetooth() {
        if (PermissionUtils.hasLocationPermissions(requireContext())) {
            viewModel.scanBLE(SERVICE_UUID)
        } else {
            PermissionUtils.requestLocationPermissions(
                requireActivity(), REQUEST_LOCATION_PERMISSION_CODE
            )
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                viewModel.enableBluetooth()
            } else {
                showError("BlueTooth Permission Denied")
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                viewModel.scanBLE(SERVICE_UUID)
            } else {
                showError("Location Permission Denied")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(ScreenTag, "Bluetooth enabled")
                showDevicesHideBlueToothConnectionLayout()
            } else {
                Log.d(ScreenTag, "Bluetooth not enabled")
            }
        }
    }

    companion object {
        val ScreenTag = "BlueToothFragment"
        private const val REQUEST_BLUETOOTH_PERMISSION_CODE = 1
        private const val REQUEST_ENABLE_BT = 2
        private const val REQUEST_LOCATION_PERMISSION_CODE = 3
        private const val SERVICE_UUID = "6e408975-b5a3-f393-e0a9-e50e24dcca9e"
    }

}
