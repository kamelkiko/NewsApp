package com.kamel.newsapp.di

import com.kamel.newsapp.data.repository.NewsRepositoryImpl
import com.kamel.newsapp.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindNewsRepository(newsRepository: NewsRepositoryImpl): NewsRepository
}