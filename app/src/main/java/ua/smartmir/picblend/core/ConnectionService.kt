package ua.smartmir.picblend.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ConnectionService {
    val networkStatus: StateFlow<NetworkStatus>
    fun cleanup()

    class Base @Inject constructor(
        @ApplicationContext private val context: Context
    ) : ConnectionService {

        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        private val _networkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.Connected)
        override val networkStatus = _networkStatus.asStateFlow()

        private val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateNetworkStatus(NetworkStatus.Connected)
            }

            override fun onLost(network: Network) {
                updateNetworkStatus(NetworkStatus.Disconnected)
            }

            override fun onUnavailable() {
                updateNetworkStatus(NetworkStatus.Disconnected)
            }
        }

        private val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        init {
            val isConnected = connectivityManager.activeNetwork?.let { network ->
                connectivityManager.getNetworkCapabilities(network)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } == true

            updateNetworkStatus(
                if (isConnected) NetworkStatus.Connected else NetworkStatus.Disconnected
            )

            connectivityManager.registerNetworkCallback(request, callback)
        }

        private fun updateNetworkStatus(status: NetworkStatus) {
            _networkStatus.update { status }
        }

        override fun cleanup() {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}

fun ConnectionService.isNetworkConnected(): Boolean {
    return networkStatus.value == NetworkStatus.Connected
}

sealed interface NetworkStatus {
    object Connected : NetworkStatus
    object Disconnected : NetworkStatus
}