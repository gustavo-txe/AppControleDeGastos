package com.example.controledegastos.data.model

enum class CategoryType(val value: String) {
    SALARY("Salário"),
    INVESTMENTS("Investimentos"),
    EXTRA_INCOME("Rendimentos Extras"),
    DEBTS("Contas"),
    FOOD("Alimentação"),
    HEALTH("Saúde"),
    TRANSPORT("Transporte"),
    LEISURE("Lazer e Entretenimento"),
    EDUCATION("Educação"),
    OTHERS("Outros");

    companion object {
        val valuesList: List<String> = entries.map { it.value }
    }
}
