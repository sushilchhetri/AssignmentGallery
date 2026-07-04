package com.assigment.assignmentgallery.common

import com.assigment.assignmentgallery.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class ErrorHandler @Inject constructor() {

    fun handleException(exception: Throwable): UiText {

        return when (exception) {

            is UnknownHostException -> {
                UiText.StringResource(R.string.error_no_internet)
            }

            is SocketTimeoutException -> {
                UiText.StringResource(R.string.error_connection_timeout)
            }

            is IOException -> {
                UiText.StringResource(R.string.error_network)
            }

            is SSLException -> {
                UiText.StringResource(R.string.error_ssl)
            }

            is HttpException -> {
                getHttpError(exception.code())
            }

            else -> {
                UiText.StringResource(R.string.error_unknown)
            }
        }
    }

    fun getHttpError(code: Int): UiText {

        return when (code) {

            400 -> UiText.StringResource(R.string.error_bad_request)

            401 -> UiText.StringResource(R.string.error_unauthorized)

            403 -> UiText.StringResource(R.string.error_forbidden)

            404 -> UiText.StringResource(R.string.error_not_found)

            408 -> UiText.StringResource(R.string.error_request_timeout)

            409 -> UiText.StringResource(R.string.error_conflict)

            422 -> UiText.StringResource(R.string.error_unprocessable_entity)

            429 -> UiText.StringResource(R.string.error_too_many_requests)

            in 500..599 -> {
                UiText.StringResource(R.string.error_server)
            }

            else -> {
                UiText.StringResource(R.string.error_unknown)
            }
        }
    }
}