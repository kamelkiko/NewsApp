package com.kamel.newsapp.presentation.screen.details

sealed interface ArticleDetailsIntent {
    data object GoBack : ArticleDetailsIntent
    data class ShareArticleUrl(val articleUrl: String) : ArticleDetailsIntent
    data class OpenBrowserToReadArticle(val articleUrl: String) : ArticleDetailsIntent
}