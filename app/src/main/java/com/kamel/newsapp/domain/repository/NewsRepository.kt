package com.kamel.newsapp.domain.repository

import com.kamel.newsapp.domain.entity.Article

interface NewsRepository {
    suspend fun getNewsArticlesByCategory(category: String): List<Article>
}