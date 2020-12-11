package com.example.shopick

import com.example.shopick.datamodels.Order
import com.example.shopick.datamodels.OrderResponse
import retrofit2.Call
import retrofit2.http.*

interface CheckoutInterface {

    @POST("orders")
    @Headers("content-type: application/json")
    fun createOrder(@Body order: Order) : Call<OrderResponse>
}