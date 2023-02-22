package com.tarun.cbcnewsappassignment.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A network monitoring service class to monitor and get the internet connectivity status.
 */
class NetworkMonitoringService : KoinComponent {
    private val networkRequest: NetworkRequest by inject()
    private val connectivityManager: ConnectivityManager by inject()
    lateinit var isNetworkConnectivityAvailable: MutableLiveData<Boolean>

    /**
     * Callback for when the network status changes.
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isNetworkConnectivityAvailable.postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isNetworkConnectivityAvailable.postValue(false)
        }
    }

    /**
     * Start monitoring the internet connectivity status.
     */
    fun monitorNetworkStatus() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    /**
     * Get the internet connectivity status.
     * @return True if connected, false otherwise.
     */
    fun getInitialConnectionStatus(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}