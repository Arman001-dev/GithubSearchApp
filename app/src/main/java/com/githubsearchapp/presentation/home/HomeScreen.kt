package com.githubsearchapp.presentation.home

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            SearchView(homeScreenViewModel, state.searchQuery)
            Icon(painter = painterResource(id = R.drawable.ic_downloads), contentDescription = null, modifier = Modifier.size(72.dp))
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
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(36.dp)
                            .clickable {
                                homeScreenViewModel.addIntent(
                                    HomeScreenIntent.DownloadRepo(
                                        item.owner?.login ?: Constants.EMPTY_STRING,
                                        item.name ?: Constants.EMPTY_STRING,
                                        item.defaultBranch ?: Constants.EMPTY_STRING
                                    )
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