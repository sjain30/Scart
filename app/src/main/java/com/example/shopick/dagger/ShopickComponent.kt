package com.example.shopick.dagger

import com.example.shopick.MainActivity
import dagger.Component
import javax.inject.Inject

@Component(modules = [NetworkModule::class])
interface ShopickComponent {

    fun injectCheckout(mainActivity: MainActivity)
}