package com.githubsearchapp.domain.repository

import com.githubsearchapp.common.model.Resource
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoTypeEnum
import com.githubsearchapp.domain.model.GitRepoItem
import kotlinx.coroutines.flow.Flow

interface GitRepository {

    suspend fun getUserRepos(username: String, type: GitRepoTypeEnum = GitRepoTypeEnum.PUBLIC): Flow<Resource<List<GitRepoItem>>>

    suspend fun downloadGitRepo(gitRepoItem: GitRepoItem): Flow<Resource<Long>>

    suspend fun getDownloadedRepositories(): Flow<Resource<List<GitRepoItem>>>

    suspend fun clearAll(): Unit
}