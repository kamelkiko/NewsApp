package com.kamel.newsapp.presentation.navigation

import com.kamel.newsapp.presentation.screen.home.ArticleState
import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    data object HomeNewsScreen

    @Serializable
    data class ArticleDetailRoute(
        val article: ArticleState
    )
}