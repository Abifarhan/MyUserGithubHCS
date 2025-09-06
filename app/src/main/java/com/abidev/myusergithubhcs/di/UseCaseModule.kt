package com.abidev.myusergithubhcs.di


import com.abidev.domain.detail.usecase.GetUserDetailUseCase
import com.abidev.domain.detail.usecase.GetUserDetailUseCaseImpl
import com.abidev.domain.home.usecase.GetDefaultUsersUseCase
import com.abidev.domain.home.usecase.GetDefaultUsersUseCaseImpl
import com.abidev.domain.home.usecase.GetUsersUseCase
import com.abidev.domain.home.usecase.GetUsersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindSearchUsersUseCase(
        impl: GetUsersUseCaseImpl
    ): GetUsersUseCase

    @Binds
    abstract fun bindGetDefaultUsersUseCase(
        impl: GetDefaultUsersUseCaseImpl
    ): GetDefaultUsersUseCase

    @Binds
    abstract fun bindGetUserDetailUseCase(impl: GetUserDetailUseCaseImpl): GetUserDetailUseCase
}