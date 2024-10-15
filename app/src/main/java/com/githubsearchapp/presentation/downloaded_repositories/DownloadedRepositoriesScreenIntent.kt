package com.githubsearchapp.presentation.downloaded_repositories

import com.githubsearchapp.presentation.base.BaseIntent

sealed class DownloadedRepositoriesScreenIntent : BaseIntent {

    data object Initial : DownloadedRepositoriesScreenIntent()

    data object ClearAll : DownloadedRepositoriesScreenIntent()
}