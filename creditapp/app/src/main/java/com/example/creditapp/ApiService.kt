package com.example.creditapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/predict")
    fun predict(@Body inputData: CreditRequest): Call<PredictionResponse>
}