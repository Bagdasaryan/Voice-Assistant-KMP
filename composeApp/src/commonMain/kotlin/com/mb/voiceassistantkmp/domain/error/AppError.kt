package com.mb.voiceassistantkmp.domain.error

sealed interface AppError {
    sealed interface Network : AppError {
        object NoInternet : Network
        object ServerError : Network
    }
    object DatabaseError : AppError
    sealed interface Speech : AppError {
        object Timeout : Speech
        object NoMatch : Speech
        object PermissionDenied : Speech
    }

    data class Unknown(val message: String?) : AppError
}
