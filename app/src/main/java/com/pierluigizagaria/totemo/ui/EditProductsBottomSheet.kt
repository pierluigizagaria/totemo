package com.pierluigizagaria.totemo.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.EncodeHintType
import com.pierluigizagaria.totemo.R
import com.pierluigizagaria.totemo.WebClient
import com.pierluigizagaria.totemo.WebServer
import com.pierluigizagaria.totemo.utils.WifiUtils
import net.glxn.qrgen.android.QRCode

class EditProductsBottomSheet : BottomSheetDialogFragment() {
    companion object {
        private val TAG = EditProductsBottomSheet::class.simpleName
    }

    private var webServer: WebServer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webServer = WebServer(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_products_sheet, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = requireView().findViewById<Button>(R.id.endButton)
        button.setOnClickListener { dismiss() }
        if (!WifiUtils.isWifiConnected(requireContext())) return
        val wifiIPAddress = WifiUtils.getWifiIPAddress(requireContext())
        val url = String.format("http://%s:%s", wifiIPAddress, 8080)
        setQRCodeUrl(url)
        val text = requireView().findViewById<TextView>(R.id.bottomSheetText)
        text.setText(R.string.qr_scan_tip)
        if (!WebClient.getInstance(requireContext()).isUpdating) {
            webServer!!.start()
        } else {
            Toast.makeText(requireContext(), "Client is updating, please wait.", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        webServer!!.stop()
    }

    fun setQRCodeUrl(url: String) {
        val qrCodeImage = requireView().findViewById<ImageView>(R.id.qrCodeImage)
        val qrCodeBitmap = QRCode.from(url)
            .withHint(EncodeHintType.MARGIN, 0)
            .withSize(500, 500)
            .bitmap()
        Glide.with(requireContext())
            .load(qrCodeBitmap)
            .into(qrCodeImage)
    }

}