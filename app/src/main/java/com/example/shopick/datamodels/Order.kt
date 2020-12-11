package com.example.shopick.datamodels

data class Order(
    val amount: Int,
    val currency: String,
    val receipt: String
)