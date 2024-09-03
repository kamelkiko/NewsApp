package com.kamel.newsapp.di

import com.kamel.newsapp.data.local.model.ArticleCollection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Singleton
    @Provides
    fun provideRealmDB(realmConfiguration: RealmConfiguration): Realm {
        return Realm.open(configuration = realmConfiguration)
    }

    @Singleton
    @Provides
    fun provideRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder(
            schema = setOf(
                ArticleCollection::class,
            )
        ).compactOnLaunch().deleteRealmIfMigrationNeeded().build()
    }
}