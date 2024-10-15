package com.githubsearchapp.data.local.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.githubsearchapp.common.utils.Constants.DOWNLOADED_GIT_REPOS
import com.githubsearchapp.domain.model.GitRepoItem
import com.githubsearchapp.domain.model.Owner

@Entity(tableName = DOWNLOADED_GIT_REPOS)
data class DownloadedGitRepoEntity(
    @PrimaryKey val name: String,
    val defaultBranch: String?,
    val description: String?,
    val svnUrl: String?,
    val ownerAvatarUrl: String?,
    val ownerLogin: String?
)

fun DownloadedGitRepoEntity.toGitRepoItem(): GitRepoItem {
    return GitRepoItem(
        name = name,
        description = description,
        svnUrl = svnUrl,
        owner = Owner(
            avatarUrl = ownerAvatarUrl,
            login = ownerLogin
        ),
        defaultBranch = defaultBranch
    )
}



