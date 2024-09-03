package com.kamel.newsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.kamel.newsapp.presentation.navigation.NewsAppNavHost
import com.kamel.newsapp.presentation.theme.NewsAppTheme
import com.kamel.newsapp.presentation.util.LocalNavigationProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CompositionLocalProvider(LocalNavigationProvider provides navController) {
                NewsAppTheme {
                    NewsAppNavHost()
                }
            }
        }
    }
}