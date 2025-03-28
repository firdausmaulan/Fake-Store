package com.fd.fakestore.di

import com.fd.fakestore.data.repository.cart.ICartRepository
import com.fd.fakestore.data.repository.product.IProductRepository
import com.fd.fakestore.data.repository.user.IUserRepository
import com.fd.fakestore.ui.cart.CartViewModel
import com.fd.fakestore.ui.checkout.CheckoutViewModel
import com.fd.fakestore.ui.login.LoginViewModel
import com.fd.fakestore.ui.product.detail.ProductDetailViewModel
import com.fd.fakestore.ui.product.list.ProductListViewModel
import com.fd.fakestore.ui.profile.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideLoginViewModel(userRepository: IUserRepository): LoginViewModel {
        return LoginViewModel(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideProfileViewModel(userRepository: IUserRepository): ProfileViewModel {
        return ProfileViewModel(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideProductListViewModel(productRepository: IProductRepository): ProductListViewModel {
        return ProductListViewModel(productRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideProductDetailViewModel(productRepository: IProductRepository, cartRepository: ICartRepository): ProductDetailViewModel {
        return ProductDetailViewModel(productRepository, cartRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCartViewModel(cartRepository: ICartRepository): CartViewModel {
        return CartViewModel(cartRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCheckoutViewModel(cartRepository: ICartRepository, userRepository: IUserRepository): CheckoutViewModel {
        return CheckoutViewModel(cartRepository, userRepository)
    }
}