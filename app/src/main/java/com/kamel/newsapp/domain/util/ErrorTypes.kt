package com.kamel.newsapp.domain.util

open class NewsAppException(message: String?) : Exception(message)

class NetworkException(override val message: String?) : NewsAppException(message)

class NotFoundException(override val message: String?) : NewsAppException(message)

class ServerErrorException(override val message: String?) : NewsAppException(message)

class UnknownErrorException(override val message: String?) : NewsAppException(message)