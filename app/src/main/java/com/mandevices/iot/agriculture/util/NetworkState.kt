package com.mandevices.iot.agriculture.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkState(val context: Context) {
    fun hasInternet(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}