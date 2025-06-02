from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from enum import Enum
import joblib
import pandas as pd
import numpy as np
import logging

logging.basicConfig(level=logging.INFO)

app = FastAPI()

# Загрузка модели и feature_list
try:
    model = joblib.load("/data/credit_model.pkl")
    feature_list = joblib.load("/data/feature_list.pkl")
except Exception as e:
    raise RuntimeError(f"Ошибка при загрузке модели или feature_list: {str(e)}")


# Enum'ы
class PersonHomeOwnership(str, Enum):
    RENT = "RENT"
    OWN = "OWN"
    MORTGAGE = "MORTGAGE"
    OTHER = "OTHER"

class LoanIntent(str, Enum):
    EDUCATION = "EDUCATION"
    MEDICAL = "MEDICAL"
    VENTURE = "VENTURE"
    PERSONAL = "PERSONAL"
    DEBTCONSOLIDATION = "DEBTCONSOLIDATION"
    HOMEIMPROVEMENT = "HOMEIMPROVEMENT"

class LoanGrade(str, Enum):
    A = "A"
    B = "B"
    C = "C"
    D = "D"
    E = "E"
    F = "F"
    G = "G"

class CBDefault(str, Enum):
    Y = "Y"
    N = "N"

class CreditRequest(BaseModel):
    person_age: int
    person_income: float
    person_emp_length: float
    person_home_ownership: PersonHomeOwnership
    loan_intent: LoanIntent
    loan_grade: LoanGrade
    loan_amnt: float
    loan_int_rate: float
    loan_percent_income: float
    cb_person_default_on_file: CBDefault
    cb_person_cred_hist_length: int


@app.post("/predict")
async def predict(request: CreditRequest):
    try:
        # Шаг 1: Словарь → DataFrame
        input_dict = request.dict()
        df_input = pd.DataFrame([input_dict])

        # Шаг 2: One-Hot кодирование
        cat_cols = ['person_home_ownership', 'loan_intent', 'loan_grade', 'cb_person_default_on_file']
        df_input = pd.get_dummies(df_input, columns=cat_cols, drop_first=True)

        # Шаг 3: Приводим к нужному формату признаков
        for col in feature_list:
            if col not in df_input.columns:
                df_input[col] = 0  # Добавляем недостающие колонки
        df_input = df_input[feature_list]  # Порядок признаков

        # Шаг 4: Предсказание
        prob = model.predict_proba(df_input)[0][1]
        pred = int(prob > 0.5)

        return {
            "probability": float(prob),
            "prediction": pred
        }

    except Exception as e:
        logging.error(f"Prediction error: {str(e)}")
        raise HTTPException(status_code=400, detail=str(e))