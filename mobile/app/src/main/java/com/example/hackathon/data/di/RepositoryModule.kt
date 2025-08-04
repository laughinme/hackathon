package com.example.hackathon.data.di

import com.example.hackathon.data.repository.impl.AuthRepositoryImpl
import com.example.hackathon.data.repository.impl.BookRepositoryImpl
import com.example.hackathon.data.repository.impl.GeographyRepositoryImpl
import com.example.hackathon.data.repository.impl.UserRepositoryImpl
import com.example.hackathon.domain.repository.AuthRepository
import com.example.hackathon.domain.repository.BookRepository
import com.example.hackathon.domain.repository.GeographyRepository
import com.example.hackathon.domain.repository.UserRepository
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
        profileRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository

    @Binds
    @Singleton
    abstract fun bindGeographyRepository(
        geographyRepositoryImpl: GeographyRepositoryImpl
    ): GeographyRepository
}