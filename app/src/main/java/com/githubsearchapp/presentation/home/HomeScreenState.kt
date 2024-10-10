package com.githubsearchapp.presentation.home

import com.githubsearchapp.common.utils.Constants
import com.githubsearchapp.domain.model.GitRepoItem
import com.githubsearchapp.presentation.base.BaseState

data class HomeScreenState(
    val searchQuery: String = Constants.EMPTY_STRING,
    val isLoading: Boolean? = null,
    val error: String? = null,
    val repoItems: List<GitRepoItem>? = null
) : BaseState
