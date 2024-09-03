package com.kamel.newsapp.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kamel.newsapp.R
import com.kamel.newsapp.domain.entity.ArticleCategory
import com.kamel.newsapp.presentation.navigation.Screens
import com.kamel.newsapp.presentation.util.LocalNavigationProvider
import kotlin.enums.EnumEntries

@Composable
fun HomeNewsScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val state by homeViewModel.state.collectAsState()
    val navController = LocalNavigationProvider.current

    HomeNewsContent(
        state = state,
        handleIntent = { intent ->
            when (intent) {
                is HomeIntent.NavigateToArticleDetails -> {
                    navController.navigate(Screens.ArticleDetailRoute(intent.article))
                }

                else -> homeViewModel.onIntent(intent)
            }
        }
    )
}

@Composable
private fun HomeNewsContent(
    state: HomeState,
    handleIntent: (HomeIntent) -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            FilterChips(
                filters = ArticleCategory.entries,
                onFilterSelected = { category ->
                    handleIntent(HomeIntent.GetArticlesByCategory(category))
                }
            )

            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }

            AnimatedVisibility(
                visible = state.errorMessage != "",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_internet),
                            contentDescription = null,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Error: ${state.errorMessage}",
                            color = Color.Red,
                            modifier = Modifier.padding(8.dp)
                        )
                        Button(onClick = { handleIntent(HomeIntent.TryAgain(state.currentCategory)) }) {
                            Text(text = stringResource(R.string.try_again))
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = state.articles.isNotEmpty() && state.isLoading.not() &&
                        state.errorMessage == "",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.articles) { article ->
                        ArticleCard(
                            articleItem = article,
                            onClickArticle = {
                                handleIntent(
                                    HomeIntent.NavigateToArticleDetails(article)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChips(
    filters: EnumEntries<ArticleCategory>,
    onFilterSelected: (ArticleCategory) -> Unit
) {
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters.size) { index ->
            val filter = filters[index]
            val isSelected = selectedFilter == filter

            FilterChip(
                text = filter.categoryName,
                isSelected = isSelected,
                onClick = {
                    selectedFilter = filter
                    onFilterSelected(filter)
                }
            )
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) Color(0xFFF44336) else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                if (isSelected.not())
                    onClick()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
private fun ArticleCard(
    articleItem: ArticleState,
    onClickArticle: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClickArticle)
            .padding(16.dp)
    ) {
        AsyncImage(
            model = articleItem.urlToImage,
            contentDescription = stringResource(R.string.article_image),
            error = painterResource(id = R.drawable.error_image),
            placeholder = painterResource(id = R.drawable.error_image),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = 0.7f
                    shadowElevation = 8.dp.toPx()
                },
            contentScale = ContentScale.FillBounds,
            clipToBounds = true
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = articleItem.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.by, articleItem.author),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = articleItem.publishedAt,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
            )
        }
    }
}