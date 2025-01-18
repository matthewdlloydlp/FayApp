package com.ml.fay.di

import com.ml.fay.repository.AuthRepository
import com.ml.fay.repository.DefaultAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthRepositoryModule {

    @Singleton
    @Binds
    fun bindsAuthRepositoryModule(
        authRepository: DefaultAuthRepository
    ): AuthRepository
}