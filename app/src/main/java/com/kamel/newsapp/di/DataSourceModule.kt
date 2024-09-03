package com.kamel.newsapp.di

import com.kamel.newsapp.data.local.RealmLocalStorageDataSource
import com.kamel.newsapp.data.remote.KtorClientDataSource
import com.kamel.newsapp.data.repository.local.LocalDataSource
import com.kamel.newsapp.data.repository.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteDataSource(
        remoteDataSource: KtorClientDataSource
    ): RemoteDataSource

    @Singleton
    @Binds
    abstract fun bindLocalDataSource(
        localDataSource: RealmLocalStorageDataSource
    ): LocalDataSource

}