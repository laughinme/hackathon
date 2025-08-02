package com.example.hackathon.data.di

import com.example.hackathon.data.repository.impl.AuthRepositoryImpl
import com.example.hackathon.data.repository.impl.ProfileRepositoryImpl
import com.example.hackathon.domain.repository.AuthRepository
import com.example.hackathon.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}