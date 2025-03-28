package com.fd.fakestore.di

import com.fd.fakestore.data.api.service.ProductApiService
import com.fd.fakestore.data.api.service.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUserApiService(): UserApiService {
        return UserApiService()
    }

    @Provides
    @Singleton
    fun provideProductApiService(): ProductApiService {
        return ProductApiService()
    }
}