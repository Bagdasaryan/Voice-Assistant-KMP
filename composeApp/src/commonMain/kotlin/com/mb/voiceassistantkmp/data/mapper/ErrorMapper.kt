package com.mb.voiceassistantkmp.data.mapper

import com.mb.voiceassistantkmp.domain.error.AppError
import kotlinx.io.IOException

fun Throwable.toError(): AppError {
    return when (this) {
        is IOException -> AppError.Network.NoInternet
        is Exception -> AppError.Network.ServerError
        else -> AppError.Unknown("Something went wrong")
    }
}

fun mapErrorToMessage(error: AppError): String {
    return when (error) {
        is AppError.Network.NoInternet -> "No internet connection. Check settings and try again."
        is AppError.Network.ServerError -> "The server is temporarily unavailable. Please try again later."

        is AppError.Speech.NoMatch -> "Speech not recognized. Please speak more clearly and try again."
        is AppError.Speech.PermissionDenied -> "Microphone access is disabled. Please enable it in the system settings."
        is AppError.Speech.Timeout -> "Voice input timed out. Tap the microphone to restart."

        is AppError.DatabaseError -> "Local storage error. Data could not be saved."
        is AppError.Unknown -> error.message ?: "An unexpected error occurred."
    }
}
