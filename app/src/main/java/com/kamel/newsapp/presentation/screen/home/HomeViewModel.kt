package com.kamel.newsapp.presentation.screen.home

import com.kamel.newsapp.domain.entity.Article
import com.kamel.newsapp.domain.entity.ArticleCategory
import com.kamel.newsapp.domain.usecase.GetNewsArticlesUseCase
import com.kamel.newsapp.presentation.base.BaseViewModel
import com.kamel.newsapp.presentation.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArticles: GetNewsArticlesUseCase
) : BaseViewModel<HomeState, HomeIntent>(HomeState()) {

    init {
        onIntent(HomeIntent.GetArticlesByCategory(state.value.currentCategory))
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetArticlesByCategory -> getArticlesByCategory(intent.category)
            is HomeIntent.ShowError -> showError(intent.error)
            is HomeIntent.TryAgain -> getArticlesByCategory(intent.category)
            is HomeIntent.ChangeCurrentCategory -> changeCurrentCategory(intent.category)
            else -> {}
        }
    }

    private fun getArticlesByCategory(category: ArticleCategory) {
        updateState { state ->
            state.copy(
                currentCategory = category,
                isLoading = true,
                errorState = null,
                errorMessage = ""
            )
        }
        tryToExecute(
            function = { getArticles(category.categoryName) },
            onSuccess = ::onGetArticlesSuccess,
            onError = ::onGetArticlesFailed
        )
    }

    private fun onGetArticlesSuccess(articles: List<Article>) {
        updateState { state ->
            state.copy(
                articles = articles.map { it.toUiState() },
                errorMessage = "",
                isLoading = false,
                errorState = null
            )
        }
    }

    private fun onGetArticlesFailed(errorState: ErrorState) {
        onIntent(HomeIntent.ShowError(errorState))
    }


    private fun changeCurrentCategory(category: ArticleCategory) {
        updateState { state ->
            state.copy(
                currentCategory = category,
                articles = emptyList(),
                errorMessage = "",
                errorState = null
            )
        }
        getArticlesByCategory(category)
    }


    private fun showError(error: ErrorState) {
        updateState { state ->
            state.copy(
                isLoading = false,
                errorState = error,
                errorMessage = when (error) {
                    is ErrorState.UnknownError -> error.message.toString()
                    is ErrorState.ServerError -> error.message.toString()
                    is ErrorState.NetworkError -> error.message.toString()
                    is ErrorState.NotFound -> error.message.toString()
                }
            )
        }
    }
}