package com.example.shopick.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.utils.ImageHandler
import com.example.shopick.R
import com.example.shopick.adapters.RecommendationAdapter
import com.example.shopick.datamodels.Product
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_scanned_item.*

class ScannedItemActivity : AppCompatActivity() {

    lateinit var product: Product
    lateinit var mDatabaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_item)


        val bardcode = intent.getStringExtra("Barcode").toString()
        mDatabaseReference = FirebaseUtil.getDatabase().getReference("ProductList").child(bardcode)
        mDatabaseReference.addListenerForSingleValueEvent(listener)

        recyclerViewRecommended.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecommendationAdapter(arrayListOf("Apple", "Mango"))
        recyclerViewRecommended.adapter = adapter
    }

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            ImageHandler().getSharedInstance(this@ScannedItemActivity)?.load(snapshot.child("image").value.toString())?.into(productImage)
            productName.text = snapshot.child("productName").value.toString()
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(localClassName, "onCancelled: ${error.message}")
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mDatabaseReference.removeEventListener(listener)
    }
}