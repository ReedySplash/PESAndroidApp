package edu.pes.laresistencia.methods

import android.content.Context
import android.net.ConnectivityManager

class NetworkMethods {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null &&
                    connectivityManager.activeNetworkInfo.isConnected
        }
    }
}