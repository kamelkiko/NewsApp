package com.kamel.newsapp.presentation.screen.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kamel.newsapp.R
import com.kamel.newsapp.presentation.screen.home.ArticleState
import com.kamel.newsapp.presentation.util.LocalNavigationProvider

@Composable
fun ArticlesDetailsScreen(article: ArticleState) {
    val navController = LocalNavigationProvider.current
    val context = LocalContext.current

    ArticlesDetailsContent(
        article,
        handleIntent = { intent ->
            when (intent) {
                ArticleDetailsIntent.GoBack -> navController.navigateUp()

                is ArticleDetailsIntent.OpenBrowserToReadArticle -> {
                    val openBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(intent.articleUrl))
                    context.startActivity(openBrowserIntent)
                }

                is ArticleDetailsIntent.ShareArticleUrl -> {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            article.url
                        )
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
            }
        }
    )
}

@Composable
private fun ArticlesDetailsContent(
    article: ArticleState,
    handleIntent: (ArticleDetailsIntent) -> Unit
) {

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        AsyncImage(
                            model = article.urlToImage,
                            contentDescription = stringResource(R.string.article_image),
                            error = painterResource(id = R.drawable.error_image),
                            placeholder = painterResource(id = R.drawable.error_image),
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            clipToBounds = true
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black
                                        ), startY = 300f
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(
                                    text = article.publishedAt,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = article.title,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(R.string.published_by, article.author),
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                )
                            }
                        }
                    }
                    Text(
                        text = article.content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                handleIntent(
                                    ArticleDetailsIntent.OpenBrowserToReadArticle(
                                        article.url
                                    )
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = stringResource(R.string.open_article),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    handleIntent(ArticleDetailsIntent.ShareArticleUrl(article.url))
                },
                containerColor = Color.Red,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(R.string.icon_share)
                )
            }

            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = stringResource(R.string.icon_back),
                tint = Color.Black,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(8.dp)
                    .clickable(
                        onClick =
                        {
                            handleIntent(ArticleDetailsIntent.GoBack)
                        }
                    )
            )
        }
    }
}