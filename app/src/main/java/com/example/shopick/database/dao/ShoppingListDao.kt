package com.example.shopick.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shopick.database.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao{

    @Insert
    suspend fun add(shoppingListEntity: ShoppingListEntity)

    @Query("Select * from ShoppingListEntity")
    fun getList():Flow<List<ShoppingListEntity>>
}