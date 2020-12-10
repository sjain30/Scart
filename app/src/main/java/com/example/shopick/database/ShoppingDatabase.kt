package com.example.shopick.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shopick.database.dao.ShoppingListDao
import com.example.shopick.database.entity.ShoppingListEntity


@Database(
    entities = [ShoppingListEntity::class],
    version = 1
)
abstract class ShoppingDatabase:RoomDatabase() {
    abstract fun getShoppingList(): ShoppingListDao

    companion object{

        @Volatile
        private var instance: ShoppingDatabase?=null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "ShoppingDatabase"
        ).build()
    }
}