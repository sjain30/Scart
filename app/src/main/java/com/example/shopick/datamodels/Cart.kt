package com.example.shopick.datamodels

import java.io.Serializable

data class Cart(
    val productName:String,
    val price:String,
    val image:String,
    val discount:String,
    val cutPrice:String,
    val quantity:String):Serializable