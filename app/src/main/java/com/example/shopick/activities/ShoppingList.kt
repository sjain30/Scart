package com.example.shopick.activities

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.adapters.ListAdapter
import com.example.shopick.R
import com.example.shopick.ShoppingListViewModel
import com.example.shopick.datamodels.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_shopping_list.*

class ShoppingList : AppCompatActivity() {
    private var listItems = ArrayList<String>()

    lateinit var shoppingListViewModel: ShoppingListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)


        shoppingListViewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)

        shoppingListViewModel.getListItems().observe(this, Observer {
            recycler_list.setHasFixedSize(true)
            recycler_list.layoutManager = LinearLayoutManager(this)
            val adapter = ListAdapter(it as java.util.ArrayList<Item>, this@ShoppingList, shoppingListViewModel)
            recycler_list.adapter = adapter
        })

        edt_name.editText?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        shoppingListViewModel.addList(edt_name.editText?.text.toString())
                        return@setOnKeyListener true
                    }
                    else -> {
                    }
                }
            }
            return@setOnKeyListener false
        }
    }
}