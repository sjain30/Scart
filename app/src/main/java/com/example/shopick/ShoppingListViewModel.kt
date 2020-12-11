package com.example.shopick

import android.util.Log
import androidx.lifecycle.*
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_shopping_list.*

class ShoppingListViewModel : ViewModel() {

    private var mDatabaseReference: DatabaseReference? = null
    var listItems:MutableLiveData<List<String>>?= null

    fun ShoppingListViewModel() {
        mDatabaseReference =
            FirebaseUtil.getDatabase().getReference("ShoppingList")
    }

//    fun getListItems() : LiveData<List<String>> {
//        if (listItems == null) {
//            listItems = MutableLiveData()
//            getList()
//        }
//        return listItems!!
//    }

    fun getList() : LiveData<List<String>>{

        listItems = MutableLiveData()
        val list =  arrayListOf<String>()

        mDatabaseReference =
            FirebaseUtil.getDatabase().getReference("ShoppingList")

        mDatabaseReference!!.child("${FirebaseAuth.getInstance().currentUser?.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (item in snapshot.children) {
                        list.add(item.value.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ShoppingList", error.toString())
                }

            })
        Log.d("TAG", "getList: $list")
        listItems?.postValue(list)
        return listItems!!
    }

    fun addList(name: String) {

        mDatabaseReference =
            FirebaseUtil.getDatabase().getReference("ShoppingList")

        mDatabaseReference!!.child("${FirebaseAuth.getInstance().currentUser?.uid}/${System.currentTimeMillis()}").setValue(name)
            .addOnSuccessListener {
                Log.d("TAG", "addList: Success")
            }
            .addOnFailureListener {
                Log.d("TAG", "addList: ${it.toString()}")
            }

    }
}