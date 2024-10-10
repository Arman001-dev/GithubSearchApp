package com.githubsearchapp.presentation.home

import com.githubsearchapp.common.model.Resource
import com.githubsearchapp.domain.repository.GitRepository
import com.githubsearchapp.presentation.base.BaseSideEffectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: GitRepository) : BaseSideEffectViewModel<HomeScreenIntent, HomeScreenState, HomeScreenSideEffect>() {

    override val effects: Channel<HomeScreenSideEffect> = Channel()
    val homeScreenEffects: Flow<HomeScreenSideEffect>
        get() = effects.consumeAsFlow()

    override val states: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())
    val homeScreenStates: StateFlow<HomeScreenState>
        get() = states

    override fun handleIntent(intent: HomeScreenIntent) {
        when (intent) {
            is HomeScreenIntent.Initial -> {}
            is HomeScreenIntent.Search -> {
                updateState(states.value.copy(searchQuery = intent.query))
                if (intent.query.isNotEmpty()) {
                    debounce<String>(dispatcher = Dispatchers.IO) {
                        repository.getUserRepos(username = it).collectLatest { response ->
                            when (response) {
                                is Resource.Error -> {
                                    updateState(states.value.copy(error = response.error, isLoading = false))
                                }

                                is Resource.Loading -> {
                                    updateState(states.value.copy(isLoading = true, error = null))
                                }

                                is Resource.Success -> {
                                    updateState(states.value.copy(isLoading = false, error = null, repoItems = response.data))
                                }
                            }
                        }
                    }.invoke(intent.query)
                } else {
                    updateState(states.value.copy(repoItems = null, error = null, isLoading = null))
                }
            }
        }
    }
}