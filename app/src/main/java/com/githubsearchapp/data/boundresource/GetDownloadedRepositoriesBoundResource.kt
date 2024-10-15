package com.githubsearchapp.data.boundresource

import com.githubsearchapp.common.utils.boundResource.FetchPolicy
import com.githubsearchapp.common.utils.boundResource.HttpBoundResource
import com.githubsearchapp.data.local.dao.GitReposDao
import com.githubsearchapp.data.local.enitity.DownloadedGitRepoEntity
import com.githubsearchapp.data.local.enitity.toGitRepoItem
import com.githubsearchapp.domain.model.GitRepoItem

class GetDownloadedRepositoriesBoundResource(private val dao: GitReposDao) : HttpBoundResource<List<DownloadedGitRepoEntity>, List<GitRepoItem>>() {

    override fun determineFetchPolicy(): FetchPolicy = FetchPolicy.CACHE_ONLY

    override fun fetchFromStorage(): List<GitRepoItem> {
        return dao.getAllDownloadedGitRepos().map { it.toGitRepoItem() }
    }
}