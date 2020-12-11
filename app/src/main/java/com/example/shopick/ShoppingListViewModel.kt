package com.example.shopick

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.shopick.datamodels.Item
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val mDatabaseReference = FirebaseUtil.getDatabase().getReference("ShoppingList")
    var listItems:MutableLiveData<List<Item>>?= null

    fun getListItems() : LiveData<List<Item>> {
        if (listItems == null) {
            listItems = MutableLiveData()
            getList()
        }
        return listItems!!
    }

    fun getList() : LiveData<List<Item>>{

        listItems = MutableLiveData()
        val list =  arrayListOf<Item>()

        mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (items in snapshot.children) {
                        val item = items.getValue(Item::class.java);
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

    fun addList(name: String) {
        val list  = listItems?.value as ArrayList<Item>
        list.add(Item(name,false))
        mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").setValue(list)
            .addOnSuccessListener {
                Log.d("TAG", "addList: Success")
            }
            .addOnFailureListener {
                Log.d("TAG", "addList: ${it.toString()}")
            }

    }

    fun removeItem(item: Item){
        val list  = listItems?.value as ArrayList<Item>
        list.remove(item)
        mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").setValue(list)
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Issue with internet", Toast.LENGTH_SHORT).show()
            }
    }

    fun flashList(list: java.util.ArrayList<Item>){
        mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").setValue(list)
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Issue with internet", Toast.LENGTH_SHORT).show()
            }
    }
}