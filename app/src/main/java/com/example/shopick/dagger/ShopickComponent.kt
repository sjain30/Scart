package com.example.shopick.dagger

import com.example.shopick.activities.TransactionActivity
import dagger.Component

@Component(modules = [NetworkModule::class])
interface ShopickComponent {

    fun injectCheckout(transactionActivity: TransactionActivity)
}