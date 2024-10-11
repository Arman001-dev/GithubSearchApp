package com.githubsearchapp.data.local.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.githubsearchapp.common.utils.Constants.DOWNLOADED_GIT_REPOS

@Entity(tableName = DOWNLOADED_GIT_REPOS)
data class DownloadedGitRepoEntity(
    @PrimaryKey val name: String,
    val ownerUsername: String,
    val defaultBranch: String
)
