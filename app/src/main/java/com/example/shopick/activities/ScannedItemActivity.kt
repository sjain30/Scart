package com.example.shopick.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.utils.ImageHandler
import com.example.shopick.R
import com.example.shopick.adapters.RecommendationAdapter
import com.example.shopick.datamodels.Cart
import com.example.shopick.datamodels.Item
import com.example.shopick.datamodels.Product
import com.example.shopick.utils.FirebaseUtil
import com.example.shopick.utils.gone
import com.example.shopick.utils.toast
import com.example.shopick.utils.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Completable
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

        add_to_cart.setOnClickListener {
            flashCardList()
        }
    }

    private fun flashCardList(){
        cartDatabaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (items in snapshot.children) {
                    val item = items.getValue(Cart::class.java);
                    item?.let { arrayList.add(it) }
                }
                arrayList.add(Cart(product.productName.toString(),product.price.toString(),product.image.toString(),product.discount.toString(),product.cutPrice.toString(),"1",null))
                cartDatabaseReference.setValue(arrayList).addOnSuccessListener {
                    toast("Item added to cart!")
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(localClassName, "onCancelled: ${error.message}")
            }

        })


    }

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (code in snapshot.children) {
                product = code.getValue(Product::class.java)!!
                if (product.barcode.equals(barcode)) {
                    recyclerViewRecommended.layoutManager =
                        LinearLayoutManager(this@ScannedItemActivity, LinearLayoutManager.HORIZONTAL, false)
                    val adapter = RecommendationAdapter(arrayListOf(product.recommend.toString()))
                    recyclerViewRecommended.adapter = adapter
                    ImageHandler().getSharedInstance(this@ScannedItemActivity)?.load(product.image)
                        ?.into(productImage)
                    productName.text = product.productName
                    productPrice.text = "Rs. ${product.price}"
                    if(product.veg?.contains("true") == true)
                        productVeg.visible()
                    else if(product.veg?.contains("false") == true) {
                        productVeg.text = "This is a non-vegetarian product"
                    }
                    else
                        productVeg.gone()

                    if(product.milk?.contains("true") == true )
                        productMilk.text = "This products contatins milk prducts"
                    else if(product.milk?.contains("true") == false )
                        productMilk.text = "This products does not contatins milk prducts"
                    else
                        productMilk.gone()

                    break
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