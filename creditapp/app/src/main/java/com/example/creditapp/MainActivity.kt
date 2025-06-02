package com.example.creditapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class MainActivity : AppCompatActivity() {

    private lateinit var ageEditText: EditText
    private lateinit var incomeEditText: EditText
    private lateinit var homeOwnershipSpinner: MaterialAutoCompleteTextView
    private lateinit var empLengthEditText: EditText
    private lateinit var loanIntentSpinner: MaterialAutoCompleteTextView
    private lateinit var loanGradeSpinner: MaterialAutoCompleteTextView
    private lateinit var loanAmountEditText: EditText
    private lateinit var interestRateEditText: EditText
    private lateinit var percentIncomeEditText: EditText
    private lateinit var defaultSpinner: MaterialAutoCompleteTextView
    private lateinit var creditHistLengthEditText: EditText
    private lateinit var calculateButton: Button

    // Массивы для отображения и значений
    private lateinit var homeOwnershipDisplay: Array<String>
    private lateinit var homeOwnershipValues: Array<String>

    private lateinit var loanIntentDisplay: Array<String>
    private lateinit var loanIntentValues: Array<String>

    private lateinit var loanGradeDisplay: Array<String>
    private lateinit var loanGradeValues: Array<String>

    private lateinit var defaultDisplay: Array<String>
    private lateinit var defaultValues: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация полей
        ageEditText = findViewById(R.id.ageEditText)
        incomeEditText = findViewById(R.id.incomeEditText)
        homeOwnershipSpinner = findViewById(R.id.homeOwnershipSpinner)
        empLengthEditText = findViewById(R.id.empLengthEditText)
        loanIntentSpinner = findViewById(R.id.loanIntentSpinner)
        loanGradeSpinner = findViewById(R.id.loanGradeSpinner)
        loanAmountEditText = findViewById(R.id.loanAmountEditText)
        interestRateEditText = findViewById(R.id.interestRateEditText)
        defaultSpinner = findViewById(R.id.defaultSpinner)
        creditHistLengthEditText = findViewById(R.id.creditHistLengthEditText)
        calculateButton = findViewById(R.id.calculateButton)

        // Загрузка массивов
        homeOwnershipDisplay = resources.getStringArray(R.array.home_ownership_display)
        homeOwnershipValues = resources.getStringArray(R.array.home_ownership_values)

        loanIntentDisplay = resources.getStringArray(R.array.loan_intent_display)
        loanIntentValues = resources.getStringArray(R.array.loan_intent_values)

        loanGradeDisplay = resources.getStringArray(R.array.loan_grade_display)
        loanGradeValues = resources.getStringArray(R.array.loan_grade_values)

        defaultDisplay = resources.getStringArray(R.array.default_on_file_display)
        defaultValues = resources.getStringArray(R.array.default_on_file_values)

        // Настройка адаптеров для выпадающих списков
        setupDropdown(homeOwnershipSpinner, homeOwnershipDisplay)
        setupDropdown(loanIntentSpinner, loanIntentDisplay)
        setupDropdown(loanGradeSpinner, loanGradeDisplay)
        setupDropdown(defaultSpinner, defaultDisplay)

        calculateButton.setOnClickListener {
            try {
                val intent = Intent(this, ResultActivity::class.java)

                // Передача данных в ResultActivity
                intent.putExtra("AGE", ageEditText.text.toString().toInt())
                intent.putExtra("INCOME", incomeEditText.text.toString().toDouble())

                // Получаем введенные данные
                val income = incomeEditText.text.toString().toDouble()
                val loanAmount = loanAmountEditText.text.toString().toFloat()

                // Вычисление процента от дохода
                val annualIncome = income * 12 // Преобразуем ежемесячный доход в годовой
                val percentIncome = (loanAmount / annualIncome) * 100

                // Поиск английского значения из выбранного элемента
                val homeOwnershipIndex = homeOwnershipDisplay.indexOf(homeOwnershipSpinner.text.toString())
                val homeOwnershipValue = if (homeOwnershipIndex != -1) homeOwnershipValues[homeOwnershipIndex] else ""

                val loanIntentIndex = loanIntentDisplay.indexOf(loanIntentSpinner.text.toString())
                val loanIntentValue = if (loanIntentIndex != -1) loanIntentValues[loanIntentIndex] else ""

                val loanGradeIndex = loanGradeDisplay.indexOf(loanGradeSpinner.text.toString())
                val loanGradeValue = if (loanGradeIndex != -1) loanGradeValues[loanGradeIndex] else ""

                val defaultIndex = defaultDisplay.indexOf(defaultSpinner.text.toString())
                val defaultValue = if (defaultIndex != -1) defaultValues[defaultIndex] else ""

                // Передача значений
                intent.putExtra("HOME_OWNERSHIP", homeOwnershipValue)
                intent.putExtra("EMP_LENGTH", empLengthEditText.text.toString().toFloat())
                intent.putExtra("LOAN_INTENT", loanIntentValue)
                intent.putExtra("LOAN_GRADE", loanGradeValue)
                intent.putExtra("LOAN_AMOUNT", loanAmountEditText.text.toString().toFloat())
                intent.putExtra("INTEREST_RATE", interestRateEditText.text.toString().toFloat())
                intent.putExtra("PERCENT_INCOME", percentIncome)
                intent.putExtra("DEFAULT", defaultValue)
                intent.putExtra("CREDIT_HIST_LENGTH", creditHistLengthEditText.text.toString().toFloat())

                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Метод для настройки адаптера выпадающего списка
    private fun setupDropdown(view: MaterialAutoCompleteTextView, items: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        view.setAdapter(adapter)
    }
}
