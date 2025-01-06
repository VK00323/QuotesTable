package com.example.data.websocket.di

import com.example.data.websocket.api.QuotesLabelApi
import com.example.data.websocket.dataSource.IQuotesLabelApiDataSource
import com.example.data.websocket.dataSource.IQuotesLabelLocalDataSource
import com.example.data.websocket.dataSource.QuotesLabelApiDataSource
import com.example.data.websocket.dataSource.QuotesLabelLocalDataSource
import com.example.data.websocket.repository.IQuotesLabelRepository
import com.example.data.websocket.repository.QuotesLabelRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface QuotesDataModule {
    companion object {

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): QuotesLabelApi {
            return retrofit.create(QuotesLabelApi::class.java)
        }
    }

    @Binds
    fun bindsQuotesLabelApiDataSource(impl: QuotesLabelApiDataSource): IQuotesLabelApiDataSource

    @Binds
    fun bindsLabelQuotesRepository(impl: QuotesLabelRepository): IQuotesLabelRepository

    @Binds
    fun bindsQuotesLabelLocalDataSource(impl: QuotesLabelLocalDataSource): IQuotesLabelLocalDataSource

}