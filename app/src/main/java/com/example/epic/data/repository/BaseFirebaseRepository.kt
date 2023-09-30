package com.example.epic.data.repository

import com.example.epic.util.Resource
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

abstract class BaseFirebaseRepository {

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