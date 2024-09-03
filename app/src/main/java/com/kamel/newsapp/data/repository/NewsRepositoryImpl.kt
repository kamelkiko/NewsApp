package com.kamel.newsapp.data.repository

import com.kamel.newsapp.data.local.mapper.toCollection
import com.kamel.newsapp.data.local.mapper.toEntity
import com.kamel.newsapp.data.local.model.ArticleCollection
import com.kamel.newsapp.data.remote.mapper.toEntity
import com.kamel.newsapp.data.repository.local.LocalDataSource
import com.kamel.newsapp.data.repository.remote.RemoteDataSource
import com.kamel.newsapp.domain.entity.Article
import com.kamel.newsapp.domain.repository.NewsRepository
import com.kamel.newsapp.domain.util.NewsAppException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NewsRepository {

    override suspend fun getNewsArticlesByCategory(category: String): List<Article> {
        return fetchData(
            remoteFetch = {
                remoteDataSource.getArticlesByCategory(category).map { it.toEntity(category) }
                    .apply { println("KAMELO: $this") }
            },
            localFetch = {
                getNewsArticlesFromLocal(category).map { it.toEntity() }
            },
            cacheData = { articles ->
                clearArticlesFromCacheByCategory(category)
                saveNewsArticlesToLocal(articles.map { it.toCollection() })
            },
            mapToEntity = { it },
        )
    }

    private suspend fun saveNewsArticlesToLocal(articles: List<ArticleCollection>) {
        localDataSource.saveArticles(articles)
    }

    private suspend fun clearArticlesFromCacheByCategory(category: String) {
        localDataSource.deleteArticlesByCategory(category)
    }

    private fun getNewsArticlesFromLocal(category: String): List<ArticleCollection> {
        return localDataSource.getSavedArticlesByCategory(category)
    }

    private suspend fun <T, R> fetchData(
        remoteFetch: suspend () -> List<T>,
        localFetch: suspend () -> List<T>,
        cacheData: suspend (List<T>) -> Unit,
        mapToEntity: (T) -> R,
        handleError: (NewsAppException) -> NewsAppException = { it },
    ): List<R> {
        return try {
            val remoteData = remoteFetch()
            if (remoteData.isNotEmpty()) {
                cacheData(remoteData)
            }
            remoteData.map { mapToEntity(it) }
        } catch (exception: NewsAppException) {
            val localData = localFetch().map { mapToEntity(it) }
            localData.ifEmpty {
                throw handleError(exception)
            }
        }
    }
}