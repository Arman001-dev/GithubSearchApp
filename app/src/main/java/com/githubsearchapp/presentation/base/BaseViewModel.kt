package com.githubsearchapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githubsearchapp.common.utils.Constants.DEFAULT_DEBOUNCE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Intent : BaseIntent, State : BaseState> : ViewModel() {
    protected abstract val states: MutableStateFlow<State>
    abstract fun handleIntent(intent: Intent)

    private val intents: MutableSharedFlow<Intent> = MutableSharedFlow()

    init {
        viewModelScope.launch {
            intents.collect {
                handleIntent(it)
            }
        }
    }


    fun addIntent(intent: Intent) {
        viewModelScope.launch {
            intents.emit(intent)
        }
    }

    protected fun updateState(state: State) {
        states.update {
            state
        }
    }

    private var debounceJob: Job? = null
    fun <T> debounce(waitMs: Long = DEFAULT_DEBOUNCE, coroutineScope: CoroutineScope = viewModelScope, dispatcher: CoroutineDispatcher = Dispatchers.Main, destinationFunction: suspend (T) -> Unit): (T) -> Unit {
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch(dispatcher) {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }
}