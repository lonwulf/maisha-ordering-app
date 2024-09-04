package com.lonwulf.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.lonwulf.core.events.NetworkChangeEvent
import org.greenrobot.eventbus.EventBus

// API 24 and above, using the ConnectivityManager.NetworkCallback
class NetworkMonitor(context: Context) {
    private val connectivityManager: ConnectivityManager
    private var networkCallback: NetworkCallback? = null

    init {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
    }

    fun registerNetworkCallback() {
        if (networkCallback == null) {
            networkCallback = object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    EventBus.getDefault().post(NetworkChangeEvent(true))
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    EventBus.getDefault().post(NetworkChangeEvent(false))
                }
            }
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager
                .registerNetworkCallback(request, networkCallback as NetworkCallback)
        }
    }

    fun unregisterNetworkCallback() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            networkCallback = null
        }
    }
}
