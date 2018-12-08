package com.example.administrator.glasshouse.api

import android.util.Log
import com.apollographql.apollo.api.Response
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: T): ApiResponse<T> {
            return ApiSuccessResponse(
                    body = response!!
            )

        }

        fun <T> createError(error: String): ApiResponse<T> {
            return ApiErrorResponse(
                    errorMessage = error
            )

        }

    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
        val body: T
) : ApiResponse<T>() {

}


data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()
