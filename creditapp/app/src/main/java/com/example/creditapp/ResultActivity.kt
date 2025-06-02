package com.example.creditapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Получаем данные из Intent
        val age = intent.getIntExtra("AGE", 0)
        val income = intent.getDoubleExtra("INCOME", 0.0).toFloat()
        val homeOwnership = intent.getStringExtra("HOME_OWNERSHIP") ?: "RENT"
        val empLength = intent.getFloatExtra("EMP_LENGTH", 0f)
        val loanIntent = intent.getStringExtra("LOAN_INTENT") ?: "PERSONAL"
        val loanGrade = intent.getStringExtra("LOAN_GRADE") ?: "B"
        val loanAmount = intent.getFloatExtra("LOAN_AMOUNT", 0f)
        val interestRate = intent.getFloatExtra("INTEREST_RATE", 0f)
        val percentIncome = intent.getFloatExtra("PERCENT_INCOME", 0f)
        val defaultOnFile = intent.getStringExtra("DEFAULT_ON_FILE") ?: "Y"
        val creditHistLength = intent.getFloatExtra("CREDIT_HIST_LENGTH", 0f)

        // Устанавливаем значения в TextView
        findViewById<TextView>(R.id.ageTextView).text = "Возраст: $age"
        findViewById<TextView>(R.id.incomeTextView).text = "Доход: $income"
        findViewById<TextView>(R.id.homeOwnershipTextView).text = "Тип жилья: $homeOwnership"
        findViewById<TextView>(R.id.empLengthTextView).text = "Стаж работы: $empLength лет"
        findViewById<TextView>(R.id.loanIntentTextView).text = "Цель кредита: $loanIntent"
        findViewById<TextView>(R.id.loanGradeTextView).text = "Класс кредита: $loanGrade"
        findViewById<TextView>(R.id.loanAmountTextView).text = "Сумма кредита: $loanAmount"
        findViewById<TextView>(R.id.interestRateTextView).text = "Процентная ставка: $interestRate"
        findViewById<TextView>(R.id.percentIncomeTextView).text = "Процент от дохода: $percentIncome"
        findViewById<TextView>(R.id.defaultOnFileTextView).text = "Дефолт в истории: $defaultOnFile"
        findViewById<TextView>(R.id.creditHistLengthTextView).text = "Длина кредитной истории: $creditHistLength лет"

        // Собираем все данные в объект
        val inputData = CreditRequest(
            person_age = age,
            person_income = income,
            person_home_ownership = homeOwnership,
            person_emp_length = empLength,
            loan_intent = loanIntent,
            loan_grade = loanGrade,
            loan_amnt = loanAmount,
            loan_int_rate = interestRate,
            loan_percent_income = percentIncome,
            cb_person_default_on_file = defaultOnFile,
            cb_person_cred_hist_length = creditHistLength
        )

        // Отправляем запрос на сервер
        RetrofitClient.apiService.predict(inputData).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val prediction = response.body()
                    val resultTextView = findViewById<TextView>(R.id.resultTextView)

                    if (prediction != null) {
                        val result = if (prediction.prediction == 1) {
                            "Кредит нельзя выдать (риск: ${prediction.probability})"
                        } else {
                            "Кредит можно выдать (риск: ${prediction.probability * 100} % )"
                        }

                        resultTextView.text = result
                    }
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                val resultTextView = findViewById<TextView>(R.id.resultTextView)
                resultTextView.text = "Ошибка при подключении к серверу: ${t.message}"
            }
        })
    }
}
