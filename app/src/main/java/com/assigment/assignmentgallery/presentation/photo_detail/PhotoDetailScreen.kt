package com.assigment.assignmentgallery.presentation.photo_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.assigment.assignmentgallery.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(

    onBackClick: () -> Unit,

    viewModel: PhotoDetailViewModel = hiltViewModel()

) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFF1A1A28),
        topBar = {

            CenterAlignedTopAppBar(

                title = {

                    Text(

                        text = "PHOTO",

                        color = Color.White,

                        style = MaterialTheme.typography.titleLarge,

                        fontWeight = FontWeight.Bold

                    )

                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(

                            imageVector = Icons.Default.ArrowBack,

                            contentDescription = stringResource(R.string.back),

                            tint = Color.White

                        )
                    }
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(

                    containerColor = Color(0xFF1A1A28),

                    titleContentColor = Color.White,

                    navigationIconContentColor = Color.White

                )

            )
        }

    ) { padding ->

        if (state.isLoading) {

            Column(

                modifier = Modifier

                    .fillMaxSize()

                    .background(Color(0xFF1A1A28))

                    .padding(padding)

                    .padding(20.dp),

                horizontalAlignment = Alignment.CenterHorizontally,

                verticalArrangement = Arrangement.Center

            ) {

                CircularProgressIndicator()
            }

        } else {

            state.photo?.let { photo ->

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)

                ) {

                    AsyncImage(

                        model = photo.url,

                        contentDescription = null,

                        modifier = Modifier

                            .fillMaxWidth()

                            .height(260.dp)

                            .clip(RoundedCornerShape(20.dp)),
                        placeholder = painterResource(R.drawable.placeholder_image),

                        error = painterResource(R.drawable.placeholder_image),

                        contentScale = ContentScale.Crop

                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(

                        text = photo.title,

                        color = Color.White,

                        style = MaterialTheme.typography.headlineSmall,

                        fontWeight = FontWeight.Bold

                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(

                        text = "#${photo.id}",

                        color = Color.Gray,

                        style = MaterialTheme.typography.bodyMedium

                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    OutlinedButton(

                        onClick = {

                            viewModel.toggleFavorite()

                        }

                    ) {

                        Icon(

                            imageVector = if (photo.isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,

                            contentDescription = null

                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(

                            text = if (photo.isFavorite)
                                "Saved to Favorites"
                            else
                                "Save to Favorites"

                        )

                    }
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Text(

                        text = "Tap the heart to save this photo.\nIt'll still be here next time you open the app.",

                        color = Color.Gray,

                        style = MaterialTheme.typography.bodyMedium

                    )
                }
            }
        }
    }
}