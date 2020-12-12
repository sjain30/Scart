package com.example.shopick.activities

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopick.datamodels.Cart
import com.example.shopick.datamodels.Item
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CartActivityViewModel:ViewModel() {

    var listItems: MutableLiveData<List<Cart>>?= null
    val mDatabaseReference = FirebaseUtil.getDatabase().getReference("Cart/${FirebaseAuth.getInstance().currentUser?.uid}")

    fun getListItems() : LiveData<List<Cart>> {
        if (listItems == null) {
            listItems = MutableLiveData()
            getList()
        }
        return listItems!!
    }

    fun getList() : LiveData<List<Cart>>{

        listItems = MutableLiveData()
        val list =  arrayListOf<Cart>()

        mDatabaseReference
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (items in snapshot.children) {
                        val item = items.getValue(Cart::class.java);
                        item?.let { list.add(it) }
                    }
                    listItems?.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ShoppingList", error.toString())
                }

            })
        Log.d("TAG", "getList: $list")
        return listItems!!
    }
    fun removeItem(){
        val list = arrayListOf<Cart>()
        mDatabaseReference.setValue(list)
    }


}