package com.mb.voiceassistantkmp.data.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.mb.voiceassistantkmp.data.mapper.toDomain
import com.mb.voiceassistantkmp.data.mapper.toError
import com.mb.voiceassistantkmp.data.remote.api.ApiService
import com.mb.voiceassistantkmp.domain.error.AppError
import com.mb.voiceassistantkmp.domain.error.Resource
import com.mb.voiceassistantkmp.domain.model.Vital
import com.mb.voiceassistantkmp.domain.repository.SpeechRecognizerRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

class SpeechRecognizerRepositoryImpl(
    private val api: ApiService,
    private val context: Context
) : SpeechRecognizerRepository {
    private var speechRecognizer: SpeechRecognizer? = null

    override suspend fun analyzeText(text: String): Resource<Vital> {
        return try {
            val analyzedText = api.analyzeText(text).toDomain()
            Resource.Success(analyzedText)
        } catch (e: Exception) {
            Resource.Error(e.toError())
        }
    }

    override fun startListening(): Flow<String> {
        return callbackFlow {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.UK)
            }

            val listener = object : RecognitionListener {
                override fun onPartialResults(partialResults: Bundle?) {
                    val data = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = data?.get(0) ?: ""
                    if (text.isNotEmpty()) {
                        trySend(text)
                    }
                }

                override fun onResults(results: Bundle?) {
                    val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = data?.get(0) ?: ""
                    trySend(text)
                    close()
                }

                override fun onError(error: Int) {
                    val appError = when (error) {
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> AppError.Speech.Timeout
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> AppError.Speech.PermissionDenied
                        else -> AppError.Unknown("Something went wrong")
                    }
                    close(Exception("Speech error: ${error}"))
                }

                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            }

            Handler(Looper.getMainLooper()).post {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(listener)
                    startListening(intent)
                }
            }

            awaitClose {
                stopListening()
            }
        }
    }

    override fun stopListening() {
        Handler(Looper.getMainLooper()).post {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
            speechRecognizer = null
        }
    }
}