package com.githubsearchapp.presentation.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

abstract class BaseSideEffectViewModel<Intent : BaseIntent, State : BaseState, Effect : BaseSideEffect> : BaseViewModel<Intent, State>() {

    protected abstract val effects: Channel<Effect>

    fun addSideEffect(effect: Effect) {
        viewModelScope.launch {
            effects.send(effect)
        }
    }
}