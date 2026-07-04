package com.assigment.assignmentgallery.common

import com.assigment.assignmentgallery.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SafeApiCall @Inject constructor(
    private val errorHandler: ErrorHandler
) {

    suspend fun <T> execute(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> Response<T>
    ): Resource<T> {

        return withContext(dispatcher) {

            try {

                val response = apiCall()

                if (response.isSuccessful) {

                    response.body()?.let {

                        Resource.Success(it)

                    } ?: Resource.Error(
                        message = UiText.StringResource(R.string.error_empty_response),
                        code = response.code()
                    )

                } else {

                    Resource.Error(
                        message = errorHandler.getHttpError(response.code()),
                        code = response.code()
                    )
                }

            } catch (exception: Exception) {

                Resource.Error(
                    message = errorHandler.handleException(exception)
                )
            }
        }
    }
}