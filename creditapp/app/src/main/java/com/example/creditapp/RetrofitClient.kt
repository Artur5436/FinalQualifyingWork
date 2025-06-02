package com.example.creditapp

// RetrofitClient.kt

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Замените на правильный URL сервера FastAPI
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)  // Здесь указываем базовый URL для FastAPI
        .addConverterFactory(GsonConverterFactory.create())  // Подключаем конвертер для JSON
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
