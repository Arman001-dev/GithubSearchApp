package com.githubsearchapp.data.remote

import com.githubsearchapp.common.model.ApiWrapper
import com.githubsearchapp.common.utils.parseResponse
import com.githubsearchapp.data.model.gitrepos.GitRepoItemDto
import com.githubsearchapp.data.model.gitrepos.GitRepoTypeEnum
import com.githubsearchapp.data.remote.apiservice.GitApiService
import javax.inject.Inject

class GitNetworkAdapter @Inject constructor(private val gitApiService: GitApiService) : GitNetworkPort {
    override suspend fun getUserRepos(username: String, type: GitRepoTypeEnum): ApiWrapper<List<GitRepoItemDto>> {
        return parseResponse {
            gitApiService.getUserRepos(username, type.type)
        }
    }
}