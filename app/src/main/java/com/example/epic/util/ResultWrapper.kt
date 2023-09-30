package com.example.epic.util

sealed class ResultWrapper<out R> {
    data class Success<out R>(val result: R) : ResultWrapper<R>()
    data class Failure(val exception: Exception) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}