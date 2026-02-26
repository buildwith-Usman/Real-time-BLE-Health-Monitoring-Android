package com.app.dbraze.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.app.dbraze.R
import com.app.dbraze.databinding.DialogBluetoothDeviceConnectedBinding

class ShowBluetoothConnectedDialog(
    context: Context,
    private val deviceName: String?,
    private val onConnectedDoneClick: () -> Unit = {},
) : Dialog(context, R.style.AlertHorizontalDialogStyle) {

    private var binding: DialogBluetoothDeviceConnectedBinding =
        DialogBluetoothDeviceConnectedBinding.inflate(
            LayoutInflater.from(context)
        )

    init {
        init()
        initView()
    }

    private fun init() {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun initView() {
        with(binding) {


            if (!deviceName.isNullOrEmpty()) {
                connectedSuccessfullyText.text = deviceName
            }


            btnDoneConnected.setOnClickListener {
                dismiss()
                onConnectedDoneClick.invoke()
            }


        }
        setBluetoothPairingSuccessfullySpannableText(deviceName)
    }

    private fun setBluetoothPairingSuccessfullySpannableText(deviceName: String?) {
        if (deviceName.isNullOrEmpty()) return // Exit if deviceName is null or empty

        val fullText = "$deviceName\nSuccessfully Paired"
        val spannableStringBuilder = SpannableStringBuilder(fullText)

        val start = fullText.indexOf(deviceName)
        val end = start + deviceName.length

        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.d_braze_primary)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        binding.connectedSuccessfullyText.text = spannableStringBuilder
    }

}