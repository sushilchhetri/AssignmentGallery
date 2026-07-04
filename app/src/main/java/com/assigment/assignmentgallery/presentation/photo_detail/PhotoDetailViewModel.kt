package com.assigment.assignmentgallery.presentation.photo_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.usecase.GetPhotoByIdUseCase
import com.assigment.assignmentgallery.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PhotoDetailState())
    val state = _state.asStateFlow()

    private val photoId: Int =
        checkNotNull(savedStateHandle["photoId"])

    init {
        getPhoto()
    }

    private fun getPhoto() {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = getPhotoByIdUseCase(photoId)) {

                is Resource.Success -> {

                    _state.value = _state.value.copy(
                        isLoading = false,
                        photo = result.data
                    )
                }

                is Resource.Error -> {

                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                Resource.Loading -> Unit
            }
        }
    }

    fun toggleFavorite() {

        viewModelScope.launch {

            val currentPhoto = _state.value.photo ?: return@launch

            toggleFavoriteUseCase(currentPhoto.id)

            _state.value = _state.value.copy(
                photo = currentPhoto.copy(
                    isFavorite = !currentPhoto.isFavorite
                )
            )
        }
    }
}