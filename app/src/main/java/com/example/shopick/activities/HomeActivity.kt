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
import com.example.shopick.utils.gone
import com.example.shopick.utils.visible
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

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

        KeyboardVisibilityEvent.setEventListener(this) { isOpen ->
            if (isOpen) {
                choose_location_layout.gone()
            } else {
                choose_location_layout.visible()
            }
        }

//        btn_transaction.setOnClickListener {
//            val intent = Intent(this,TransactionActivity::class.java)
//            startActivity(intent)
//        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getStoresList().observe(this, Observer {
            storesList = arrayListOf()
            storesList?.addAll(it)
            Log.d("TAG", "onCreate: $storesList")
        })

        store_listing.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this,TransactionActivity::class.java)
            startActivity(intent)
        }


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