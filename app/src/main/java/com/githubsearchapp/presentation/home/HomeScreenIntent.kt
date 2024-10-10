package com.githubsearchapp.presentation.home

import com.githubsearchapp.presentation.base.BaseIntent

sealed class HomeScreenIntent : BaseIntent {

    data object Initial : HomeScreenIntent()

    data class Search(val query: String) : HomeScreenIntent()
}