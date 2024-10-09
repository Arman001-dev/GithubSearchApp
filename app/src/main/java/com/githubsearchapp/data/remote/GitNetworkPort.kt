package com.githubsearchapp.data.remote

import com.githubsearchapp.common.model.ApiWrapper
import com.githubsearchapp.data.model.gitrepos.GitRepoItemDto
import com.githubsearchapp.data.model.gitrepos.GitRepoTypeEnum

interface GitNetworkPort {
    suspend fun getUserRepos(username: String, type: GitRepoTypeEnum): ApiWrapper<List<GitRepoItemDto>>
}