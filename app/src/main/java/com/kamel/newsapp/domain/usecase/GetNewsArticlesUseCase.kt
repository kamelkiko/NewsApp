package com.kamel.newsapp.domain.usecase

import com.kamel.newsapp.domain.entity.Article
import com.kamel.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsArticlesUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(category: String): List<Article> {
        return newsRepository.getNewsArticlesByCategory(category)
    }
}