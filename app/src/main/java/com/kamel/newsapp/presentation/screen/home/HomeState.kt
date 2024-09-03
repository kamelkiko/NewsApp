package com.kamel.newsapp.presentation.screen.home

import com.kamel.newsapp.domain.entity.Article
import com.kamel.newsapp.domain.entity.ArticleCategory
import com.kamel.newsapp.presentation.base.ErrorState
import kotlinx.serialization.Serializable

data class HomeState(
    val isLoading: Boolean = false,
    val currentCategory: ArticleCategory = ArticleCategory.GENERAL,
    val articles: List<ArticleState> = emptyList(),
    val errorMessage: String = "",
    val errorState: ErrorState? = null
)

@Serializable
data class ArticleState(
    val author: String = "",
    val content: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val sourceName: String = "",
    val title: String = "",
    val url: String = "",
    val urlToImage: String = "",
)

fun Article.toUiState(): ArticleState {
    return ArticleState(
        title = title,
        content = content,
        author = author,
        description = description,
        urlToImage = imageUrl,
        url = url,
        sourceName = source.name,
        publishedAt = publishedAt
    )
}