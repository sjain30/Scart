package com.example.shopick

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.example.shopick.dagger.DaggerShopickComponent
import com.example.shopick.dagger.ShopickComponent

class ShopickApplication : MultiDexApplication() {

    companion object {
        lateinit var shopickComponent: ShopickComponent

        //to get instance of application in a particular activity
        fun get(activity: Activity): ShopickApplication {
            return activity.application as ShopickApplication
        }

        //to get the component anywhere in the app
        fun getComponent(): ShopickComponent {
            return shopickComponent
        }
    }

    override fun onCreate() {
        super.onCreate()

        shopickComponent = DaggerShopickComponent.create()
    }
}