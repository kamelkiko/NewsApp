package com.kamel.newsapp.data.remote

import com.kamel.newsapp.data.remote.model.ArticleDto
import com.kamel.newsapp.data.remote.model.ServerResponse
import com.kamel.newsapp.data.repository.remote.RemoteDataSource
import com.kamel.newsapp.data.util.NewsAppApiRoutes
import com.kamel.newsapp.domain.util.NetworkException
import com.kamel.newsapp.domain.util.NotFoundException
import com.kamel.newsapp.domain.util.ServerErrorException
import com.kamel.newsapp.domain.util.UnknownErrorException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import java.io.IOException
import javax.inject.Inject

class KtorClientDataSource @Inject constructor(
    private val client: HttpClient
) : RemoteDataSource {

    override suspend fun getArticlesByCategory(category: String): List<ArticleDto> {
        return tryToExecute<ServerResponse<List<ArticleDto>>> {
            get("${NewsAppApiRoutes.ARTICLES_BY_CATEGORY}/$category/us.json")
        }.data ?: throw NotFoundException("Not found articles")
    }

    private suspend inline fun <reified T> tryToExecute(
        method: HttpClient.() -> HttpResponse
    ): T {
        try {
            return client.method().body()
        } catch (e: ClientRequestException) {
            throw UnknownErrorException(e.message)
        } catch (e: RedirectResponseException) {
            throw NotFoundException(e.message)
        } catch (e: ServerResponseException) {
            throw ServerErrorException(e.message)
        } catch (e: IOException) {
            throw NetworkException("No internet found")
        } catch (e: Exception) {
            throw UnknownErrorException("Something wrong happened please try again!")
        }
    }
}