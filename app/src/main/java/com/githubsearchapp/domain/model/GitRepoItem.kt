package com.githubsearchapp.domain.model

import com.githubsearchapp.common.utils.Constants
import com.githubsearchapp.data.local.enitity.DownloadedGitRepoEntity
import java.io.Serializable


data class GitRepoItem(
    val name: String?,
    val description: String?,
    val svnUrl: String?,
    val owner: Owner?,
    val defaultBranch: String?,
    val downloadId: Long? = null
) : Serializable

data class Owner(
    val avatarUrl: String?,
    val login: String?,
) : Serializable

fun GitRepoItem.toGitRepoEntity(): DownloadedGitRepoEntity {
    return DownloadedGitRepoEntity(
        name = name ?: Constants.EMPTY_STRING,
        description = description,
        svnUrl = svnUrl,
        ownerAvatarUrl = owner?.avatarUrl,
        ownerLogin = owner?.login,
        defaultBranch = defaultBranch
    )
}