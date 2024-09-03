package com.kamel.newsapp.data.repository.remote

import com.kamel.newsapp.data.remote.model.ArticleDto

interface RemoteDataSource {
    suspend fun getArticlesByCategory(category: String): List<ArticleDto>
}