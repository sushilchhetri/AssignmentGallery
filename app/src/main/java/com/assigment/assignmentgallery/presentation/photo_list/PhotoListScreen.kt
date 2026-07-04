package com.assigment.assignmentgallery.presentation.photo_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PhotoListScreen(

    onPhotoClick: (Int) -> Unit,

    onCreatePostClick: () -> Unit,

    onUploadImageClick: () -> Unit,

    viewModel: PhotoListViewModel = hiltViewModel()

) {

    val state by viewModel.state.collectAsStateWithLifecycle()


    val listState = rememberLazyListState()

    LaunchedEffect(listState) {

        snapshotFlow {

            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index

        }.distinctUntilChanged()

            .collect { index ->

                if (index == state.photos.lastIndex) {

                    viewModel.loadMorePhotos()
                }
            }
    }
    Scaffold(
        containerColor = Color(0xFF1A1A28),
        floatingActionButton = {

            FloatingActionButton(

                onClick = {
                    onCreatePostClick()

                },
                containerColor = Color(0xFF6C63FF),

                contentColor = Color.White

            ) {

                Icon(

                    imageVector = Icons.Default.Add,

                    contentDescription = "+"

                )
            }
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A28))
                .padding(innerPadding)
        )
        {

            LazyColumn(

                modifier = Modifier.fillMaxSize(),

                state = listState,

                verticalArrangement = Arrangement.spacedBy(14.dp),

                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 24.dp,
                    bottom = 90.dp
                )

            ) {

                item {

                    Text(

                        text = "Gallery",

                        color = Color.White,

                        style = MaterialTheme.typography.headlineLarge,

                        fontWeight = FontWeight.Bold,

                        modifier = Modifier.padding(bottom = 20.dp)

                    )

                }
                item {

                    OutlinedTextField(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),

                        value = state.searchQuery,

                        onValueChange = {

                            viewModel.onSearchQueryChanged(it)

                        },

                        placeholder = {

                            Text(

                                text = "Search photos",

                                color = Color.Gray

                            )

                        },

                        leadingIcon = {

                            Icon(

                                imageVector = Icons.Default.Search,

                                contentDescription = null,

                                tint = Color.Gray

                            )

                        },

                        singleLine = true,

                        shape = RoundedCornerShape(16.dp),

                        colors = OutlinedTextFieldDefaults.colors(

                            focusedContainerColor = Color(0xFF242438),

                            unfocusedContainerColor = Color(0xFF242438),

                            focusedBorderColor = Color.Transparent,

                            unfocusedBorderColor = Color.Transparent,

                            focusedTextColor = Color.White,

                            unfocusedTextColor = Color.White,

                            cursorColor = Color.White

                        )

                    )

                }

                itemsIndexed(

                    state.photos,

                    key = { _, photo -> photo.id }

                ) { _, photo ->

                    PhotoListItem(

                        photo = photo,

                        onClick = {

                            onPhotoClick(photo.id)

                        }

                    )
                }

                if (state.isLoadingMore) {

                    item {

                        Box(

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),

                            contentAlignment = Alignment.Center

                        ) {

                            CircularProgressIndicator()

                        }
                    }
                }
            }

            if (state.isLoading) {

                CircularProgressIndicator(

                    modifier = Modifier.align(Alignment.Center)

                )
            }
        }
    }


}