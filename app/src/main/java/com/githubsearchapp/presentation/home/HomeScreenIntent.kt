package com.githubsearchapp.presentation.home

import com.githubsearchapp.domain.model.GitRepoItem
import com.githubsearchapp.presentation.base.BaseIntent

sealed class HomeScreenIntent : BaseIntent {

    data object Initial : HomeScreenIntent()

    data class Search(val query: String) : HomeScreenIntent()

    data class DownloadRepo(val gitRepoItem: GitRepoItem) : HomeScreenIntent()
    data class RepoDownloaded(val gitRepoItem: GitRepoItem) : HomeScreenIntent()
    data class RepoDownloadFailed(val gitRepoItem: GitRepoItem) : HomeScreenIntent()
}