package com.githubsearchapp.presentation.downloaded_repositories

import com.githubsearchapp.domain.model.GitRepoItem
import com.githubsearchapp.presentation.base.BaseState

data class DownloadedRepositoriesScreenState(
    val downloadedItems: List<GitRepoItem>? = null
) : BaseState
