package com.kamel.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kamel.newsapp.presentation.screen.details.ArticlesDetailsScreen
import com.kamel.newsapp.presentation.screen.home.ArticleState
import com.kamel.newsapp.presentation.screen.home.HomeNewsScreen
import com.kamel.newsapp.presentation.util.LocalNavigationProvider
import kotlin.reflect.typeOf

@Composable
fun NewsAppNavHost() {
    val navController = LocalNavigationProvider.current
    NavHost(
        navController = navController,
        startDestination = Screens.HomeNewsScreen,
    ) {
        composable<Screens.HomeNewsScreen> {
            HomeNewsScreen()
        }
        composable<Screens.ArticleDetailRoute>(
            typeMap = mapOf(
                typeOf<ArticleState>() to NewsAppNavType.ArticleType
            )
        ) {
            val arguments = it.toRoute<Screens.ArticleDetailRoute>()
            ArticlesDetailsScreen(arguments.article)
        }
    }
}