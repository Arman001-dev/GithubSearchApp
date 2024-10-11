package com.githubsearchapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.githubsearchapp.data.local.dao.GitReposDao
import com.githubsearchapp.data.local.enitity.DownloadedGitRepoEntity

@Database(entities = [DownloadedGitRepoEntity::class], version = 1)
abstract class GitReposDatabase : RoomDatabase() {
    abstract fun gitReposDao(): GitReposDao
}