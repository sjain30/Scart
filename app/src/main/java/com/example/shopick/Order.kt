package com.example.shopick

data class Order(
    val amount: Int,
    val currency: String,
    val receipt: String
)