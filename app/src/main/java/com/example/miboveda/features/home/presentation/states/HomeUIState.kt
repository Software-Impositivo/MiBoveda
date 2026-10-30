package com.example.miboveda.features.home.presentation.states

import androidx.compose.runtime.Immutable

/** Valores en 0f..1f relativos a la altura del gráfico. */
@Immutable
data class MonthBar(val label: String, val income: Float, val expense: Float)

@Immutable
data class HomeUiState(
    val userName: String,
    val year: String,
    val totalSpent: String,
    val growth: String,
    val receiptCount: String,
    val vat: String,
    val netBalance: String,
    val months: List<MonthBar>,
)

internal val SampleHomeState = HomeUiState(
    userName = "Camila",
    year = "2026",
    totalSpent = "$ 4.286.900",
    growth = "+8%",
    receiptCount = "37",
    vat = "$ 482.310",
    netBalance = "+$ 1.240.000",
    months = listOf(
        MonthBar("Ene", 0.58f, 0.77f),
        MonthBar("Feb", 0.67f, 0.70f),
        MonthBar("Mar", 0.48f, 0.90f),
        MonthBar("Abr", 0.77f, 0.61f),
        MonthBar("May", 0.86f, 0.74f),
        MonthBar("Jun", 0.95f, 0.83f),
    ),
)