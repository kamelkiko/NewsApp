package com.kamel.newsapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamel.newsapp.domain.util.NetworkException
import com.kamel.newsapp.domain.util.NotFoundException
import com.kamel.newsapp.domain.util.ServerErrorException
import com.kamel.newsapp.domain.util.UnknownErrorException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent>(initialState: State) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    abstract fun onIntent(intent: Intent)

    protected fun <T> tryToExecute(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (ErrorState) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Job {
        return runWithErrorCheck(onError, dispatcher = dispatcher) {
            val result = function()
            onSuccess(result)
        }
    }

    protected fun <T> tryToCollect(
        function: suspend () -> Flow<T>,
        onNewValue: (T) -> Unit,
        onError: (ErrorState) -> Unit,
    ): Job {
        return runWithErrorCheck(onError) {
            function().distinctUntilChanged().collectLatest {
                onNewValue(it)
            }
        }
    }

    protected fun updateState(updater: (State) -> State) {
        _state.update(updater)
    }

    private fun runWithErrorCheck(
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        function: suspend () -> Unit,
    ): Job {
        return inScope.launch(dispatcher) {
            try {
                function()
            } catch (exception: NetworkException) {
                onError(ErrorState.NetworkError(exception.message))
            } catch (exception: ServerErrorException) {
                onError(ErrorState.ServerError(exception.message))
            } catch (exception: NotFoundException) {
                onError(ErrorState.NotFound(exception.message))
            } catch (exception: UnknownErrorException) {
                onError(ErrorState.UnknownError(exception.message))
            } catch (exception: Exception) {
                onError(ErrorState.UnknownError(exception.message))
            }
        }
    }

    protected fun launchDelayed(duration: Long, block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            delay(duration)
            block()
        }
    }
}