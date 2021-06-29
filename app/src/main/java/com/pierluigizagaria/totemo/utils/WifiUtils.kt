package com.pierluigizagaria.totemo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder

object WifiUtils {
    fun getWifiIPAddress(context: Context): String? {
        val wifiManager = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ipAddress = wifiManager.connectionInfo.ipAddress
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipAddress = Integer.reverseBytes(ipAddress)
        }
        val ipByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
        return try {
            InetAddress.getByAddress(ipByteArray).hostAddress
        } catch (ex: UnknownHostException) {
            null
        }
    }

    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }
}