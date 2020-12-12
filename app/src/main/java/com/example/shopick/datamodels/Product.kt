package com.example.shopick.datamodels

import java.io.Serializable

class Product(
    val productName: String? = null,
    val price: String? = null,
    val image: String? = null,
    val discount: String? = null,
    val cutPrice: String? = null,
    val type: String? = null,
    val ingredients: ArrayList<String>? = null
) : Serializable