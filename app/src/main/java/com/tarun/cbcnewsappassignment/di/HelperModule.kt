package com.tarun.cbcnewsappassignment.di

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val helperModule = module {
    factory {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    single { androidApplication().getSystemService(ConnectivityManager::class.java) as ConnectivityManager }
}