package com.example.quotes.di

import com.example.core.IWebSocketManager
import com.example.data.websocket.repository.QuotesUpdatesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object QuotesFeatureModule {

    @Provides
    fun provideQuotesUpdatesRepository(webSocketManager: IWebSocketManager): QuotesUpdatesRepository {
        return QuotesUpdatesRepository(webSocketManager = webSocketManager)
    }
}