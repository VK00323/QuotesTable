package com.example.quotes.utils


fun positiveOrNegativeTransformedString(amount: Double): String {
    val string = amount.toBigDecimal().toString()
    return if (amount > 0) {
        "+$string"
    } else {
        string
    }
}