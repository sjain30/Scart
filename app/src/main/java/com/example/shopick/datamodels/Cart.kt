package com.example.shopick.datamodels

import java.io.Serializable

data class Cart(
    val productName: String? = null,
    val price: String? = null,
    val image: String? = null,
    val discount: String? = null,
    val cutPrice: String? = null,
    var quantity: String? = null,
    var ingredients: ArrayList<String>? = null
):Serializable