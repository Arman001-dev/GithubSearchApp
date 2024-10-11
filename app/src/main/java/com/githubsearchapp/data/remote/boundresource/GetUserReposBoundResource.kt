package com.githubsearchapp.data.remote.boundresource

import com.githubsearchapp.common.model.ApiWrapper
import com.githubsearchapp.common.utils.boundResource.HttpBoundResource
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoItemDto
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoTypeEnum
import com.githubsearchapp.data.remote.model.gitrepos.toGitRepoItem
import com.githubsearchapp.data.remote.GitNetworkPort
import com.githubsearchapp.domain.model.GitRepoItem

class GetUserReposBoundResource(
    private val gitNetworkPort: GitNetworkPort, private val username: String, private val gitRepoTypeEnum: GitRepoTypeEnum
) : HttpBoundResource<ApiWrapper<List<GitRepoItemDto>>, List<GitRepoItem>>() {

    override suspend fun fetchFromNetwork(): ApiWrapper<List<GitRepoItemDto>> {
        return gitNetworkPort.getUserRepos(username, gitRepoTypeEnum)
    }

    override fun processResponse(response: ApiWrapper<List<GitRepoItemDto>>?, resultType: List<GitRepoItem>?): List<GitRepoItem>? {
        return when (response) {
            is ApiWrapper.Success -> response.data?.map { it.toGitRepoItem() }
            else -> null
        }
    }
}