package com.example.shopick.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.ListAdapter
import com.example.shopick.R
import com.example.shopick.ShoppingListViewModel
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_shopping_list.*

class ShoppingList : AppCompatActivity() {
    private var listItems = ArrayList<String>()

    lateinit var shoppingListViewModel: ShoppingListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        shoppingListViewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)

        shoppingListViewModel.getList().observe(this, Observer {
            recycler_list.setHasFixedSize(true)
            recycler_list.layoutManager = LinearLayoutManager(this)
            val adapter = ListAdapter(it as java.util.ArrayList<String>, this@ShoppingList)
            recycler_list.adapter = adapter
        })

        btn_create_list.setOnClickListener {
            shoppingListViewModel.addList(edt_name.text.toString())
        }
    }
}