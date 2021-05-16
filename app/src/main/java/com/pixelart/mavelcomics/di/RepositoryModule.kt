package com.pixelart.mavelcomics.di

import com.pixelart.mavelcomics.repository.ComicsRepository
import com.pixelart.mavelcomics.repository.ComicsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsComicsRepository(comicsRepositoryImpl: ComicsRepositoryImpl): ComicsRepository
}
