package com.mb.voiceassistantkmp.di

import androidx.room.Room
import com.mb.voiceassistantkmp.data.local.database.AppDatabase
import com.mb.voiceassistantkmp.data.remote.api.ApiService
import com.mb.voiceassistantkmp.data.repository.PatientRepositoryImpl
import com.mb.voiceassistantkmp.data.repository.SpeechRecognizerRepositoryImpl
import com.mb.voiceassistantkmp.domain.repository.PatientRepository
import com.mb.voiceassistantkmp.domain.repository.SpeechRecognizerRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun platformModule() = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "assistant_db"
        ).build()
    }
    single { get<AppDatabase>().patientDao() }
    single { get<AppDatabase>().vitalDao() }
    single { get<AppDatabase>().actualDataDao() }
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }, contentType = ContentType.Any)
            }
        }
    }
    single { ApiService(get()) }

    single<PatientRepository> { PatientRepositoryImpl(get(), get(), get(), get()) }
    single<SpeechRecognizerRepository> { SpeechRecognizerRepositoryImpl(get(), get()) }
}
