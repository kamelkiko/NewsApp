package com.kamel.newsapp.presentation.screen.home

import com.kamel.newsapp.domain.entity.ArticleCategory
import com.kamel.newsapp.presentation.base.ErrorState

sealed interface HomeIntent {
    data class NavigateToArticleDetails(val article: ArticleState) : HomeIntent
    data class ChangeCurrentCategory(val category: ArticleCategory) : HomeIntent
    data class GetArticlesByCategory(val category: ArticleCategory) : HomeIntent
    data class TryAgain(val category: ArticleCategory) : HomeIntent
    data class ShowError(val error: ErrorState) : HomeIntent
}