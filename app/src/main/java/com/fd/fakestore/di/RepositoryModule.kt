package com.fd.fakestore.di

import com.fd.fakestore.data.api.service.ProductApiService
import com.fd.fakestore.data.api.service.UserApiService
import com.fd.fakestore.data.local.database.dao.CartDao
import com.fd.fakestore.data.local.database.dao.ProductDao
import com.fd.fakestore.data.local.preference.AppPreference
import com.fd.fakestore.data.repository.cart.CartRepositoryImpl
import com.fd.fakestore.data.repository.cart.ICartRepository
import com.fd.fakestore.data.repository.product.IProductRepository
import com.fd.fakestore.data.repository.product.ProductRepositoryImpl
import com.fd.fakestore.data.repository.user.IUserRepository
import com.fd.fakestore.data.repository.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService,
        appPreference: AppPreference
    ): IUserRepository {
        return UserRepositoryImpl(userApiService, appPreference)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        productApiService: ProductApiService,
        productDao: ProductDao
    ): IProductRepository {
        return ProductRepositoryImpl(productApiService, productDao)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao
    ): ICartRepository {
        return CartRepositoryImpl(cartDao)
    }
}