package com.githubsearchapp.data.remote.apiservice

import com.githubsearchapp.data.model.gitrepos.GitRepoItemDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApiService {
    @GET("users/{username}/repos?")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("type") type: String
    ): Response<List<GitRepoItemDto>>
}