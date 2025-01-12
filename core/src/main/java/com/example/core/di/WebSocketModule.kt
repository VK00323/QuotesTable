package com.example.core.di

import com.example.core.webscoket.BaseWebSocketEvent
import com.example.core.webscoket.IWebSocketManager
import com.example.core.webscoket.WebSocketEventAdapter
import com.example.core.webscoket.WebSocketManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.EXPRESSION
)
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class WebSocketUrl

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @WebSocketUrl
    fun provideWebSocketUrl(): String = "wss://wss.tradernet.com/"

    @Provides
    @Singleton
    fun provideWebSocketManager(okHttpClient: OkHttpClient, gson: Gson): IWebSocketManager {
        return WebSocketManager(
            @WebSocketUrl provideWebSocketUrl(),
            okHttpClient,
            gson,
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(BaseWebSocketEvent::class.java, WebSocketEventAdapter())
            .create()
    }
}