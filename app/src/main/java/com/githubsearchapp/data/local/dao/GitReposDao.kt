package com.githubsearchapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubsearchapp.common.utils.Constants.DOWNLOADED_GIT_REPOS
import com.githubsearchapp.data.local.enitity.DownloadedGitRepoEntity

@Dao
interface GitReposDao {

    @Query("SELECT * FROM $DOWNLOADED_GIT_REPOS")
    fun getAllDownloadedGitRepos(): List<DownloadedGitRepoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDownloadedGitRepo(gitRepo: DownloadedGitRepoEntity)

    @Delete
    fun deleteDownloadedGitRepo(gitRepo: DownloadedGitRepoEntity)

    @Query("DELETE FROM $DOWNLOADED_GIT_REPOS")
    suspend fun deleteAll()
}