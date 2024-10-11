package com.githubsearchapp.domain.model

import java.io.Serializable


data class GitRepoItem(
    val name: String?,
    val description: String?,
    val svnUrl: String?,
    val owner: Owner?,
    val defaultBranch:String?
) : Serializable

data class Owner(
    val avatarUrl: String?,
    val login: String?,
) : Serializable