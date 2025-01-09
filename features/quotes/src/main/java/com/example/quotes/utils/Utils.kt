package com.example.quotes.utils

import androidx.compose.ui.graphics.Color

fun getChangeColor(value: Double?): Color {
    return if (value != null) {
        if (value > 0) Color.Green else if (value < 0) Color.Red else Color.Gray
    } else Color.Red
}

fun positiveOrNegativeTransformedString(amount: Double): String {
    val string = amount.toBigDecimal().toString()
    return if (amount > 0) {
        "+$string"
    } else {
        string
    }
}