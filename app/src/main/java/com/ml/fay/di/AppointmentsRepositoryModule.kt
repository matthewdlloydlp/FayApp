package com.ml.fay.di

import com.ml.fay.repository.AppointmentsRepository
import com.ml.fay.repository.DefaultAppointmentsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface AppointmentsRepositoryModule {

    @Singleton
    @Binds
    fun bindsAppointmentsRepository(
        appointmentsRepository: DefaultAppointmentsRepository
    ): AppointmentsRepository
}