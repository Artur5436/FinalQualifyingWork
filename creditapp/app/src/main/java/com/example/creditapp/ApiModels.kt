package com.example.creditapp

data class CreditRequest(
    val person_age: Int,
    val person_income: Float,
    val person_home_ownership: String,
    val person_emp_length: Float,
    val loan_intent: String,
    val loan_grade: String,
    val loan_amnt: Float,
    val loan_int_rate: Float,
    val loan_percent_income: Float,
    val cb_person_default_on_file: String,
    val cb_person_cred_hist_length: Float
)

data class PredictionResponse(
    val probability: Float,
    val prediction: Int
)