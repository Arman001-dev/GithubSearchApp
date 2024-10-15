package com.githubsearchapp.presentation.downloaded_repositories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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

@Composable
fun DownloadedRepositoriesScreen(navController: NavController, downloadedRepositoriesScreenViewModel: DownloadedRepositoriesScreenViewModel = hiltViewModel()) {
    val state = downloadedRepositoriesScreenViewModel.homeScreenStates.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        downloadedRepositoriesScreenViewModel.addIntent(DownloadedRepositoriesScreenIntent.Initial)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.downloads),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = stringResource(id = R.string.clear_all),
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        downloadedRepositoriesScreenViewModel.addIntent(DownloadedRepositoriesScreenIntent.ClearAll)
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.surface)
        Spacer(modifier = Modifier.height(16.dp))
        GitReposLazyColumn(state = state)
    }
}


@Composable
fun GitReposLazyColumn(state: DownloadedRepositoriesScreenState) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    if (!state.downloadedItems.isNullOrEmpty()) {
        LazyColumn {
            items(state.downloadedItems) { item ->
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
                    }
                }
            }
        }
    } else {
        Text(
            text = stringResource(id = R.string.no_data_found),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}