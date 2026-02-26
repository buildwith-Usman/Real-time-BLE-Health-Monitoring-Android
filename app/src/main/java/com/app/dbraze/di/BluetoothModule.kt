package com.app.dbraze.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.app.dbraze.data.repository.BluetoothManager
import com.app.dbraze.data.repository.BluetoothManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    // Provides BluetoothAdapter as a singleton dependency
    @Provides
    @Singleton
    fun provideBluetoothAdapter(): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter()
    }

    // Provides BluetoothManager implementation
    @Provides
    @Singleton
    fun provideBluetoothManager(
        bluetoothAdapter: BluetoothAdapter,
        @ApplicationContext context: Context // Injecting the application context
    ): BluetoothManager {
        return BluetoothManagerImpl(bluetoothAdapter, context)
    }
}
