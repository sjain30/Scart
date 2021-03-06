package com.example.shopick.datamodels

import java.io.Serializable

class Product(
    val barcode: String? =null,
    val productName: String? = null,
    val price: String? = null,
    val image: String? = null,
    val discount: String? = null,
    val cutPrice: String? = null,
    val type: String? = null,
    val ingredients: ArrayList<String>? = null,
    val veg: String? = null,
    val milk: String?=null,
    val recommend: String? = null
) : Serializable