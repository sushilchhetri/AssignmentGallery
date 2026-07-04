package com.assigment.assignmentgallery.common

sealed class Resource<out T> {

    data object Loading : Resource<Nothing>()

    data class Success<T>(
        val data: T
    ) : Resource<T>()

    data class Error(
        val message: UiText,
        val code: Int? = null
    ) : Resource<Nothing>()
}