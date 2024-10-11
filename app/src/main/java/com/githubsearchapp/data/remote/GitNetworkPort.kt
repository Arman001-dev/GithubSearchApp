package com.githubsearchapp.data.remote

import com.githubsearchapp.common.model.ApiWrapper
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoItemDto
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoTypeEnum
import kotlinx.serialization.json.JsonObject

interface GitNetworkPort {
    suspend fun getUserRepos(username: String, type: GitRepoTypeEnum): ApiWrapper<List<GitRepoItemDto>>
    suspend fun downloadGitRepo(username: String, repo: String, defaultBranch: String): ApiWrapper<Long>
}