package com.mb.voiceassistantkmp.domain.error

sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Error(val error: AppError) : Resource<Nothing>
}
