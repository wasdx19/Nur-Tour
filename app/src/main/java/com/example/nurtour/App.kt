package com.example.nurtour

import android.app.Application
import com.example.nurtour.di.networkModule
import com.example.nurtour.di.repositoryModule
import com.example.nurtour.di.useCaseModule
import com.example.nurtour.di.viewModelModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey("3567fe57-a8b5-400e-aa28-27fdb07a918a")
        startKoin {
            androidContext(applicationContext)
            modules(networkModule, repositoryModule, useCaseModule, viewModelModule )
        }
    }
}