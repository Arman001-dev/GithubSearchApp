package com.githubsearchapp.domain.repository

import com.githubsearchapp.common.model.Resource
import com.githubsearchapp.data.model.gitrepos.GitRepoTypeEnum
import com.githubsearchapp.domain.model.GitRepoItem
import kotlinx.coroutines.flow.Flow

interface GitRepository {

    suspend fun getUserRepos(username: String, type: GitRepoTypeEnum): Flow<Resource<List<GitRepoItem>>>
}