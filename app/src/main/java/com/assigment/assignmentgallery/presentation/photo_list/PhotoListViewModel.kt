package com.assigment.assignmentgallery.presentation.photo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.model.Photo
import com.assigment.assignmentgallery.domain.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 50
    }

    private val _state = MutableStateFlow(PhotoListState())
    val state = _state.asStateFlow()

    /**
     * Complete API Response
     */
    private val allPhotos = mutableListOf<Photo>()

    /**
     * Search Result
     */
    private val filteredPhotos = mutableListOf<Photo>()

    /**
     * Current Page
     */
    private var currentIndex = 0

    init {
        getPhotos()
    }

    fun getPhotos() {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = getPhotosUseCase()) {

                is Resource.Success -> {

                    allPhotos.clear()
                    allPhotos.addAll(result.data)

                    filteredPhotos.clear()
                    filteredPhotos.addAll(result.data)

                    currentIndex = 0

                    loadMorePhotos(
                        isFirstLoad = true
                    )
                }

                is Resource.Error -> {

                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                else -> Unit
            }
        }
    }

    fun loadMorePhotos(
        isFirstLoad: Boolean = false
    ) {

        if (_state.value.isLoadingMore)
            return

        if (!_state.value.hasMoreData)
            return

        _state.value = _state.value.copy(
            isLoadingMore = true
        )

        val endIndex = (currentIndex + PAGE_SIZE)
            .coerceAtMost(filteredPhotos.size)

        val newList = if (isFirstLoad) {

            filteredPhotos
                .subList(0, endIndex)
                .toList()

        } else {

            _state.value.photos +
                    filteredPhotos
                        .subList(currentIndex, endIndex)
                        .toList()

        }

        currentIndex = endIndex

        _state.value = _state.value.copy(
            isLoading = false,
            isLoadingMore = false,
            photos = newList,
            hasMoreData = currentIndex < filteredPhotos.size
        )
    }

    fun onSearchQueryChanged(
        query: String
    ) {

        _state.value = _state.value.copy(
            searchQuery = query
        )

        filteredPhotos.clear()

        if (query.isBlank()) {

            filteredPhotos.addAll(allPhotos)

        } else {

            filteredPhotos.addAll(

                allPhotos.filter { photo ->

                    photo.title.contains(
                        query,
                        ignoreCase = true
                    ) ||

                            photo.id.toString().contains(query)

                }

            )
        }

        currentIndex = 0

        _state.value = _state.value.copy(
            hasMoreData = true,
            photos = emptyList()
        )

        loadMorePhotos(
            isFirstLoad = true
        )
    }
}