package com.githubsearchapp.data.boundresource

import com.githubsearchapp.common.model.ApiWrapper
import com.githubsearchapp.common.utils.boundResource.FetchPolicy
import com.githubsearchapp.common.utils.boundResource.HttpBoundResource
import com.githubsearchapp.data.local.dao.GitReposDao
import com.githubsearchapp.data.remote.GitNetworkPort
import com.githubsearchapp.domain.model.GitRepoItem
import com.githubsearchapp.domain.model.toGitRepoEntity

class DownloadGitRepoBoundResource(
    private val gitNetworkPort: GitNetworkPort,
    private val gitRepoItem: GitRepoItem,
    private val dao: GitReposDao
) : HttpBoundResource<ApiWrapper<Long>, Long>() {

    override suspend fun fetchFromNetwork(): ApiWrapper<Long> {
        return gitNetworkPort.downloadGitRepo(gitRepoItem.owner?.login, gitRepoItem.name, gitRepoItem.defaultBranch)
    }

    override fun processResponse(response: ApiWrapper<Long>?, resultType: Long?): Long? {
        return when (response) {
            is ApiWrapper.Success -> response.data
            else -> null
        }
    }

    override fun determineFetchPolicy(): FetchPolicy = FetchPolicy.NETWORK_AND_CACHE

    override fun saveNetworkResult(result: Long?) {
        super.saveNetworkResult(result)
        dao.insertDownloadedGitRepo(gitRepoItem.toGitRepoEntity())
    }
}