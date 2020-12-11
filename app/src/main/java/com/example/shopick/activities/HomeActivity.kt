package com.example.shopick.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shopick.HomeViewModel
import com.example.shopick.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    var storesList: ArrayList<String>? = null
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_shop.setOnClickListener {
            val intent = Intent(this,ShoppingList::class.java)
            startActivity(intent)
        }

        btn_transaction.setOnClickListener {
            val intent = Intent(this,TransactionActivity::class.java)
            startActivity(intent)
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getStoresList().observe(this, Observer {
            storesList = arrayListOf()
            storesList?.addAll(it)
            Log.d("TAG", "onCreate: $storesList")
        })


        search_textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    store_listing.adapter = ArrayAdapter<String>(
                        this@HomeActivity,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        arrayListOf<String>()
                    )
                } else {
                    val list = arrayListOf<String>()
                    for (item in storesList!!) {
                        if (item.contains(s.toString(),true))
                            list.add(item)
                    }
                    store_listing.adapter = ArrayAdapter<String>(
                        this@HomeActivity,
                        android.R.layout.simple_list_item_1, android.R.id.text1, list
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Restricting start with space
                if (search_textInputEditText.text.toString()
                        .startsWith(" ")
                ) search_textInputEditText.setText(
                    ""
                )
            }

        })
    }
}