package com.assigment.assignmentgallery.presentation.create_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.assigment.assignmentgallery.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CreatePostScreen(
    onCloseClick: () -> Unit,
    viewModel: CreatePostViewModel = hiltViewModel()

) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val photoPicker = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.PickVisualMedia()

    ) { uri ->

        viewModel.onImageSelected(uri)

    }

    Scaffold(

        containerColor = Color(0xFF1A1A28),

        snackbarHost = {

            SnackbarHost(
                hostState = snackbarHostState
            )

        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
        {

            LazyColumn(

                modifier = Modifier.fillMaxSize(),

                verticalArrangement = Arrangement.spacedBy(12.dp),

                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 24.dp,
                    bottom = 24.dp
                )

            ) {

                item {

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.SpaceBetween,

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Text(

                            text = stringResource(R.string.new_post),

                            color = Color.White,

                            style = MaterialTheme.typography.headlineLarge,

                            fontWeight = FontWeight.Bold

                        )

                        IconButton(

                            onClick = {

                                onCloseClick()

                            }

                        ) {

                            Icon(

                                imageVector = Icons.Default.Close,

                                contentDescription = "Close",

                                tint = Color.White

                            )

                        }

                    }

                }

                item {

                    OutlinedTextField(

                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF242438),
                            unfocusedContainerColor = Color(0xFF242438),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        value = state.title,

                        onValueChange = {

                            viewModel.updateTitle(it)

                        },

                        label = {

                            Text(
                                stringResource(R.string.title)
                            )

                        }

                    )
                }

                item {

                    OutlinedTextField(

                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF242438),
                            unfocusedContainerColor = Color(0xFF242438),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        value = state.body,

                        onValueChange = {

                            viewModel.updateBody(it)

                        },

                        label = {

                            Text(
                                stringResource(R.string.body)
                            )

                        },

                        minLines = 5

                    )
                }
                item {

                    Button(

                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF),
                            contentColor = Color.White
                        ),
                        enabled = !state.isUploading,
                        shape = RoundedCornerShape(16.dp),
                        onClick = {

                            photoPicker.launch(

                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )

                            )

                        }

                    ) {

                        Icon(

                            imageVector = Icons.Default.AddCircle,

                            contentDescription = null

                        )

                        Text(
                            text = stringResource(R.string.select_photo)
                        )

                    }

                }

                item {

                    state.selectedImageUri?.let { uri ->

                        AsyncImage(

                            model = uri,

                            contentDescription = null,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)

                        )

                    }

                }

                item {

                    Button(

                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {

                            viewModel.createPost()

                        }

                    ) {

                        Text(
                            stringResource(R.string.create_post)
                        )

                    }
                }

                item {

                    Text(
                        text = stringResource(R.string.your_posts),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                }
                items(state.createdPosts) { post ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF242438)
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if (post.imageUri != null) {

                                AsyncImage(

                                    model = post.imageUri,

                                    contentDescription = null,

                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(12.dp))

                                )

                            } else {

                                Icon(

                                    imageVector = Icons.Default.AccountCircle,

                                    contentDescription = null,

                                    modifier = Modifier.size(70.dp)

                                )

                            }

                            Spacer(
                                modifier = Modifier.width(16.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = post.title,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = post.body,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2
                                )

                            }

                        }

                    }

                }
            }

            if (state.isLoading || state.isUploading) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }
            val context = LocalContext.current

            LaunchedEffect(state.uploadResult) {

                if (state.uploadResult != null) {

                    snackbarHostState.showSnackbar(

                        message = context.getString(R.string.post_created_successfully)

                    )

                }

            }



            LaunchedEffect(state.error) {

                state.error?.let {

                    snackbarHostState.showSnackbar(

                        message = it.asString(context)

                    )

                }

            }
        }
    }
}