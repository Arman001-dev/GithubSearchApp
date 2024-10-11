package com.githubsearchapp.data.repository

import com.githubsearchapp.common.model.Resource
import com.githubsearchapp.data.local.dao.GitReposDao
import com.githubsearchapp.data.remote.GitNetworkPort
import com.githubsearchapp.data.remote.boundresource.DownloadGitRepoBoundResource
import com.githubsearchapp.data.remote.boundresource.GetUserReposBoundResource
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoTypeEnum
import com.githubsearchapp.domain.model.GitRepoItem
import com.githubsearchapp.domain.repository.GitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GitRepositoryImpl @Inject constructor(private val gitNetworkPort: GitNetworkPort, private val dao: GitReposDao) : GitRepository {

    override suspend fun getUserRepos(username: String, type: GitRepoTypeEnum): Flow<Resource<List<GitRepoItem>>> {
        return GetUserReposBoundResource(gitNetworkPort, username, type).invoke()
    }

    override suspend fun downloadGitRepo(username: String, repo: String, defaultBranch: String): Flow<Resource<Long>> {
        return DownloadGitRepoBoundResource(gitNetworkPort, username, repo, defaultBranch,dao).invoke()
    }
}