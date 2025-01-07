package com.example.quotestable

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.example.core.AppLifecycleObserver
import com.example.core.IWebSocketManager
import com.example.core.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var webSocketManager: IWebSocketManager

    override fun onCreate() {
        super.onCreate()
        networkMonitoring()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(webSocketManager))
    }

    private fun networkMonitoring() {
        CoroutineScope(Dispatchers.Default).launch {
            networkMonitor.networkStatus().collect { isConnected ->
                if (isConnected) {
                    webSocketManager.connect()
                } else {
                    webSocketManager.disconnect()
                }
                Log.d("NetworkMonitor", "Network status: $isConnected")
            }
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 128 * 128)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }

}