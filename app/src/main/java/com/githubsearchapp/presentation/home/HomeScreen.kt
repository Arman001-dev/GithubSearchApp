package com.githubsearchapp.presentation.home

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.githubsearchapp.R
import com.githubsearchapp.common.utils.Constants
import com.githubsearchapp.common.utils.openCustomTab
import com.githubsearchapp.presentation.navigation.routes.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, homeScreenViewModel: HomeScreenViewModel = hiltViewModel()) {
    val focusManager = LocalFocusManager.current
    val state = homeScreenViewModel.homeScreenStates.collectAsStateWithLifecycle().value

    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures {
                focusManager.clearFocus()
            }
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SearchView(homeScreenViewModel, state.searchQuery)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_downloads),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.navigate(Screens.DownloadedRepositories.route)
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (state.repoItems.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.no_data_found))
            }
        } else {
            GitReposLazyColumn(homeScreenViewModel, state)
        }
    }
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(homeScreenViewModel: HomeScreenViewModel, searchQuery: String = Constants.EMPTY_STRING) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    SearchBar(
        modifier = Modifier
            .focusRequester(focusRequester),
        query = searchQuery,
        onQueryChange = { query ->
            homeScreenViewModel.addIntent(HomeScreenIntent.Search(query))
        },
        onSearch = { query ->
            homeScreenViewModel.addIntent(HomeScreenIntent.Search(query))
            focusManager.clearFocus()
        },
        active = false,
        onActiveChange = {},
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary
            )
        ),
        placeholder = {
            Text(text = stringResource(id = R.string.find_a_repository))
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty())
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        homeScreenViewModel.addIntent(HomeScreenIntent.Search(Constants.EMPTY_STRING))
                        focusRequester.requestFocus()
                    },
                )
        }
    ) {}
}

@Composable
fun GitReposLazyColumn(homeScreenViewModel: HomeScreenViewModel, state: HomeScreenState) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    LazyColumn {
        items(state.repoItems ?: listOf()) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        focusManager.clearFocus()
                        context.openCustomTab(item.svnUrl ?: return@clickable)
                    },
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                ) {
                    AsyncImage(modifier = Modifier.size(72.dp), model = item.owner?.avatarUrl, contentDescription = null)
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1F), verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = item.name?.uppercase() ?: Constants.EMPTY_STRING, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.description ?: Constants.EMPTY_STRING, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 16.sp)
                    }
                    if (item.downloadId != null) {
                        var progress by remember { mutableIntStateOf(0) }
                        MonitorDownloadProgress(
                            context = context,
                            downloadId = item.downloadId,
                            onProgressUpdate = {
                                progress = it
                            },
                            onDownloadComplete = {
                                homeScreenViewModel.addIntent(HomeScreenIntent.RepoDownloaded(item))
                            },
                            onDownloadFailed = {
                                homeScreenViewModel.addIntent(HomeScreenIntent.RepoDownloadFailed(item))
                            }
                        )
                        CircularProgressIndicator(
                            progress = {
                                progress.toFloat() / 100
                            },
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(20.dp)
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(20.dp)
                                .clickable {
                                    homeScreenViewModel.addIntent(
                                        HomeScreenIntent.DownloadRepo(item)
                                    )
                                },
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonitorDownloadProgress(
    context: Context,
    downloadId: Long,
    onProgressUpdate: (Int) -> Unit,
    onDownloadComplete: () -> Unit,
    onDownloadFailed: () -> Unit
) {
    val handler = remember { Handler(Looper.getMainLooper()) }

    val progress = remember { mutableIntStateOf(0) }

    val runnable = object : Runnable {
        @SuppressLint("Range")
        override fun run() {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                val downloadedSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))

                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress.intValue = 100
                        onDownloadComplete()
                    }

                    DownloadManager.STATUS_RUNNING -> {
                        if (totalSize > 0) {
                            val progressPercent = (downloadedSize * 100 / totalSize).toInt()
                            progress.intValue = progressPercent
                            onProgressUpdate(progressPercent)
                        }
                    }

                    else -> {
                        progress.intValue = 0
                        onDownloadFailed()
                    }
                }

                cursor.close()

                if (status != DownloadManager.STATUS_SUCCESSFUL && status != DownloadManager.STATUS_FAILED) {
                    handler.postDelayed(this, 500)
                }
            }
        }
    }

    handler.post(runnable)
}