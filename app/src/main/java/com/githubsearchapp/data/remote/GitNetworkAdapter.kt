package com.githubsearchapp.data.remote

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.githubsearchapp.R
import com.githubsearchapp.common.model.ApiWrapper
import com.githubsearchapp.common.utils.Constants
import com.githubsearchapp.common.utils.Constants.MIMETYPE_ZIP
import com.githubsearchapp.common.utils.parseResponse
import com.githubsearchapp.data.remote.apiservice.GitApiService
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoItemDto
import com.githubsearchapp.data.remote.model.gitrepos.GitRepoTypeEnum
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GitNetworkAdapter @Inject constructor(@ApplicationContext private val context: Context, private val gitApiService: GitApiService) : GitNetworkPort {
    override suspend fun getUserRepos(username: String, type: GitRepoTypeEnum): ApiWrapper<List<GitRepoItemDto>> {
        return parseResponse {
            gitApiService.getUserRepos(username, type.type)
        }
    }

    override suspend fun downloadGitRepo(username: String?, repo: String?, defaultBranch: String?): ApiWrapper<Long> {
        val request: DownloadManager.Request = DownloadManager.Request(
            Uri.parse("${Constants.GIT_API_BASE_URL}repos/${username}/${repo}/zipball/${defaultBranch}")
        )
        with(request) {
            setTitle(repo)
            setMimeType(MIMETYPE_ZIP)
            setDescription(String.format(context.getString(R.string.download_file_description), repo))
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "$repo$MIMETYPE_ZIP"
            )
        }
        val manager: DownloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return try {
            ApiWrapper.Success(manager.enqueue(request))
        } catch (e: Exception) {
            ApiWrapper.Error(code = -1, error = e.message)
        }
    }
}