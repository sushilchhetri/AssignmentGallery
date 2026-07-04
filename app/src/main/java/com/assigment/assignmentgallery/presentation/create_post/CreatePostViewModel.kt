package com.assigment.assignmentgallery.presentation.create_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assigment.assignmentgallery.R
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.common.UiText
import com.assigment.assignmentgallery.domain.usecase.CreatePostUseCase
import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import android.net.Uri
import com.assigment.assignmentgallery.domain.usecase.UploadPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val repository: GalleryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePostState())
    val state = _state.asStateFlow()

    fun updateTitle(
        title: String
    ) {

        _state.value = _state.value.copy(
            title = title
        )
    }

    fun updateBody(
        body: String
    ) {

        _state.value = _state.value.copy(
            body = body
        )
    }
    fun onImageSelected(uri: Uri?) {

        _state.value = _state.value.copy(
            selectedImageUri = uri
        )

    }

    fun clearSelectedImage() {

        _state.value = _state.value.copy(
            selectedImageUri = null
        )

    }

    fun createPost() {

        if (_state.value.title.isBlank()) {

            _state.value = _state.value.copy(
                error = UiText.StringResource(
                    R.string.error_title_required
                )
            )

            return
        }

        if (_state.value.body.isBlank()) {

            _state.value = _state.value.copy(
                error = UiText.StringResource(
                    R.string.error_body_required
                )
            )

            return
        }

        val currentState = _state.value

        val selectedImage = currentState.selectedImageUri
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                isUploading = selectedImage != null,
                error = null
            )

            if (selectedImage != null) {

                when (val uploadResult = uploadPhotoUseCase(selectedImage)) {

                    is Resource.Success -> {

                        _state.value = _state.value.copy(
                            isUploading = false,
                            uploadResult = uploadResult.data
                        )
                    }

                    is Resource.Error -> {

                        _state.value = _state.value.copy(
                            isLoading = false,
                            isUploading = false,
                            error = uploadResult.message
                        )

                        return@launch
                    }

                    Resource.Loading -> Unit
                }
            }

            when (

                val result = createPostUseCase(

                    title = currentState.title,

                    body = currentState.body,

                    imageUri = currentState.selectedImageUri?.toString()

                )

            ) {

                is Resource.Success -> {

                    _state.value = _state.value.copy(

                        title = "",

                        body = "",

                        selectedImageUri = null,

                        uploadResult = null,

                        isLoading = false,

                        isUploading = false,

                        error = null,

                        createdPosts = repository.getCreatedPosts()

                    )
                }

                is Resource.Error -> {

                    _state.value = _state.value.copy(

                        isLoading = false,
                        isUploading = false,

                        error = result.message
                    )
                }

                Resource.Loading -> Unit
            }
        }
    }
}