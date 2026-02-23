package com.example.controledegastos.data.model

enum class FlowType(val value: String) {
    INFLOW("Entrada"),
    OUTFLOW("Saída");

    companion object {
        fun fromValue(value: String): FlowType = entries.firstOrNull { it.value == value } ?: INFLOW
    }
}
