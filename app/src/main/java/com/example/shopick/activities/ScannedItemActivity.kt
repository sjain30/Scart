package com.example.shopick.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.utils.ImageHandler
import com.example.shopick.R
import com.example.shopick.adapters.RecommendationAdapter
import com.example.shopick.datamodels.Cart
import com.example.shopick.datamodels.Item
import com.example.shopick.datamodels.Product
import com.example.shopick.utils.FirebaseUtil
import com.example.shopick.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_scanned_item.*

class ScannedItemActivity : AppCompatActivity() {

    lateinit var product: Product
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var barcode: String
    lateinit var cartDatabaseReference: DatabaseReference
    val arrayList = arrayListOf<Cart>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_item)


        barcode = intent.getStringExtra("Barcode").toString()
        mDatabaseReference = FirebaseUtil.getDatabase().getReference("ProductList")
        mDatabaseReference.addListenerForSingleValueEvent(listener)
        cartDatabaseReference = FirebaseUtil.getDatabase().getReference("Cart/${FirebaseAuth.getInstance().currentUser?.uid}")

        recyclerViewRecommended.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecommendationAdapter(arrayListOf("Apple", "Mango"))
        recyclerViewRecommended.adapter = adapter

        add_to_cart.setOnClickListener {
            flashCardList()
        }
    }

    private fun flashCardList() {
        cartDatabaseReference.addListenerForSingleValueEvent(cartListener)
        arrayList.add(Cart(product.productName.toString(),product.price.toString(),product.image.toString(),product.discount.toString(),product.cutPrice.toString(),"0",null))
        cartDatabaseReference.setValue(arrayList).addOnSuccessListener {
            startActivity(Intent(this,CartActivity::class.java))
        }

    }

    private val cartListener = object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            arrayList.clear()
            for (items in snapshot.children) {
                val item = items.getValue(Cart::class.java);
                item?.let { arrayList.add(it) }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(localClassName, "onCancelled: ${error.message}")
        }

    }

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (code in snapshot.children) {
                product = code.getValue(Product::class.java)!!
                if (product.barcode.equals(barcode)) {
                    ImageHandler().getSharedInstance(this@ScannedItemActivity)?.load(product.image)
                        ?.into(productImage)
                    productName.text = product.productName
                }
            }
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