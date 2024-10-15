package com.githubsearchapp.presentation.downloaded_repositories

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadedRepositoriesScreenViewModel @Inject constructor(private val repository: GitRepository) : BaseSideEffectViewModel<DownloadedRepositoriesScreenIntent, DownloadedRepositoriesScreenState, DownloadedRepositoriesScreenSideEffect>() {

    override val effects: Channel<DownloadedRepositoriesScreenSideEffect> = Channel()
    val homeScreenEffects: Flow<DownloadedRepositoriesScreenSideEffect>
        get() = effects.consumeAsFlow()

    override val states: MutableStateFlow<DownloadedRepositoriesScreenState> = MutableStateFlow(DownloadedRepositoriesScreenState())
    val homeScreenStates: StateFlow<DownloadedRepositoriesScreenState>
        get() = states

    override fun handleIntent(intent: DownloadedRepositoriesScreenIntent) {
        when (intent) {
            is DownloadedRepositoriesScreenIntent.Initial -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.getDownloadedRepositories().collectLatest { response ->
                        when (response) {
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                updateState(states.value.copy(downloadedItems = response.data))
                            }
                        }
                    }
                }
            }

            DownloadedRepositoriesScreenIntent.ClearAll -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.clearAll()
                    updateState(states.value.copy(downloadedItems = null))
                }
            }
        }
    }
}