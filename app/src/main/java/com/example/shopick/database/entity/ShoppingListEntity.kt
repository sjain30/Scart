package com.example.shopick.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingListEntity(
    val name:String,
    val list:ArrayList<String>
)
{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}