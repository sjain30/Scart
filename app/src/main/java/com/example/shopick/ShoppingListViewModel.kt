package com.example.shopick

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class ShoppingListViewModel : ViewModel() {

    private var mDatabaseReference: DatabaseReference? = null

    fun addList(name: String) {

        mDatabaseReference =
            FirebaseUtil.getDatabase().getReference("ShoppingList")

        mDatabaseReference!!.child("${FirebaseAuth.getInstance().currentUser?.uid}/$name/${System.currentTimeMillis()}").setValue("")
            .addOnSuccessListener {
                Log.d("TAG", "addList: Sucess")
            }
            .addOnFailureListener {
                Log.d("TAG", "addList: ${it.toString()}")
            }

    }
}