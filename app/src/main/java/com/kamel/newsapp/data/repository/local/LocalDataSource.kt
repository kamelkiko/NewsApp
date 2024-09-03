package com.kamel.newsapp.data.repository.local

import com.kamel.newsapp.data.local.model.ArticleCollection

interface LocalDataSource {
    suspend fun saveArticles(articles: List<ArticleCollection>)
    fun getSavedArticlesByCategory(category: String): List<ArticleCollection>
    suspend fun deleteArticlesByCategory(category: String)
}