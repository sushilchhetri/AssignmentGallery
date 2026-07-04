package com.assigment.assignmentgallery.presentation.upload_photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.usecase.UploadPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadPhotoViewModel @Inject constructor(
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UploadPhotoState())
    val state = _state.asStateFlow()

    fun onImageSelected(
        imageUri: Uri?
    ) {

        _state.value = _state.value.copy(
            selectedImageUri = imageUri,
            uploadResult = null,
            error = null
        )
    }

    fun uploadPhoto() {

        val imageUri = _state.value.selectedImageUri ?: return

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isUploading = true,
                error = null
            )

            when (
                val result = uploadPhotoUseCase(imageUri)
            ) {

                is Resource.Success -> {

                    _state.value = _state.value.copy(
                        isUploading = false,
                        uploadResult = result.data
                    )
                }

                is Resource.Error -> {

                    _state.value = _state.value.copy(
                        isUploading = false,
                        error = result.message
                    )
                }

                Resource.Loading -> Unit
            }
        }
    }
}