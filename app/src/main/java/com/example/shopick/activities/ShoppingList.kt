package com.example.shopick.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shopick.R
import com.example.shopick.ShoppingListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_shopping_list.*

class ShoppingList : AppCompatActivity() {

    lateinit var shoppingListViewModel: ShoppingListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        shoppingListViewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)

        btn_create_list.setOnClickListener {
            shoppingListViewModel.addList(edt_name.text.toString())
        }
    }
}