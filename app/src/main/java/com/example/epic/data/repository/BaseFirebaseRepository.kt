package com.example.epic.data.repository

import com.example.epic.util.NetworkResult
import com.example.epic.util.Resource
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

abstract class BaseFirebaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error("Api call failed $errorMessage")

    suspend fun <T> safeFirebaseCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall())
            } catch (e: HttpException) {
                Resource.Error(message = e.localizedMessage ?: "HTTP Error")
            } catch (e: IOException) {
                Resource.Error(message = "Check Your Internet Connection")
            } catch (e: Exception) {
                Resource.Error(message = e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}