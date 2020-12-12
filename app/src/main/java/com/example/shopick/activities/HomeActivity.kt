package com.example.shopick.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.HomeViewModel
import com.example.shopick.R
import com.example.shopick.adapters.StoresAdapter
import com.example.shopick.utils.gone
import com.example.shopick.utils.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.stores_bottom_sheet.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class HomeActivity : AppCompatActivity() {

    var storesList: ArrayList<String>? = null
    lateinit var homeViewModel: HomeViewModel
    private var sheetBehavior: BottomSheetBehavior<RelativeLayout>? = null

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
                sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                choose_location_layout.visible()
            }
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getStoresList().observe(this, Observer {
            storesList = arrayListOf()
            storesList?.addAll(it)
            loadInAdapter(it as ArrayList<String>)
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
                    storesList?.let { loadInAdapter(it) }
//                    store_listing.adapter = ArrayAdapter<String>(
//                        this@HomeActivity,
//                        android.R.layout.simple_list_item_1,
//                        android.R.id.text1,
//                        arrayListOf<String>()
//                    )
                } else {
                    val list = arrayListOf<String>()
                    for (item in storesList!!) {
                        if (item.contains(s.toString(),true))
                            list.add(item)
                    }
                    loadInAdapter(list)
//                    store_listing.adapter = ArrayAdapter<String>(
//                        this@HomeActivity,
//                        android.R.layout.simple_list_item_1, android.R.id.text1, list
//                    )
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

        setupFilterBottomSheet()
    }

    private fun setupFilterBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(stores_bottom_sheet)

        sheetBehavior?.isHideable = false
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                BottomSheetBehavior.STATE_EXPANDED
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

//                        bottom_stores_list.setHasFixedSize(true)
//                        bottom_stores_list.layoutManager= LinearLayoutManager(this@HomeActivity)
//                        val adapter = storesList?.let { StoresAdapter(it) }
//                        bottom_stores_list.adapter = adapter

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }

        })
    }

    private fun loadInAdapter(list: ArrayList<String>) {
        bottom_stores_list.setHasFixedSize(true)
        bottom_stores_list.layoutManager= LinearLayoutManager(this@HomeActivity)
        val adapter = StoresAdapter(list,this)
        bottom_stores_list.adapter = adapter
    }
}