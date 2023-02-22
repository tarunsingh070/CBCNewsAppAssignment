package com.tarun.cbcnewsappassignment

import android.app.Application
import com.tarun.cbcnewsappassignment.di.dataModule
import com.tarun.cbcnewsappassignment.di.helperModule
import com.tarun.cbcnewsappassignment.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CBCNewsAppAssignmentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    /**
     * Initializes and starts the Koin framework.
     */
    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@CBCNewsAppAssignmentApplication)
            modules(dataModule, viewModelModule, helperModule)
        }
    }
}