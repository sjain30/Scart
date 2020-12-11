package com.example.shopick

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {

    private var mDatabaseReference: DatabaseReference? = null
    var storeItems: MutableLiveData<List<String>>?= null

    fun getStoresList() : LiveData<List<String>> {
        if (storeItems == null) {
            storeItems = MutableLiveData()
            getStores()
        }
        return storeItems!!
    }

    fun getStores(){

        val list =  arrayListOf<String>()

        mDatabaseReference =
            FirebaseUtil.getDatabase().getReference("Stores")

        mDatabaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (item in snapshot.children) {
                        list.add(item.value.toString())
                    }
                    storeItems?.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Stores", error.toString())
                }

            })
    }
}