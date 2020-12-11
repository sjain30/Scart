package com.example.shopick

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_shopping_list.*

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val mDatabaseReference = FirebaseUtil.getDatabase().getReference("ShoppingList")
    var listItems:MutableLiveData<List<String>>?= null

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

        mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (item in snapshot.children) {
                        list.add(item.value.toString())
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

    fun addList(name: String) {
        val list  = listItems?.value as ArrayList<String>
        list.add(name)
        mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").setValue(list)
            .addOnSuccessListener {
                Log.d("TAG", "addList: Success")
            }
            .addOnFailureListener {
                Log.d("TAG", "addList: ${it.toString()}")
            }

    }

    fun removeItem(name:String){
        val list  = listItems?.value as ArrayList<String>
        list.remove(name)
        mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").setValue(list)
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Issue with internet", Toast.LENGTH_SHORT).show()
            }
    }
}