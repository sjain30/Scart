package com.example.shopick.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.CheckoutInterface
import com.example.shopick.R
import com.example.shopick.ShopickApplication
import com.example.shopick.adapters.CartAdapter
import com.example.shopick.dagger.DaggerShopickComponent
import com.example.shopick.dagger.ShopickComponent
import com.example.shopick.datamodels.Cart
import com.example.shopick.datamodels.Order
import com.example.shopick.datamodels.OrderResponse
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class CartActivity : AppCompatActivity(), PaymentResultListener {

    @Inject
    lateinit var retrofit: Retrofit
    lateinit var cartActivityViewModel: CartActivityViewModel
    var total = 0
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartActivityViewModel = ViewModelProvider(this).get(CartActivityViewModel::class.java)

        ShopickApplication.getComponent().injectCheckout(this)
        val toolbar = findViewById<Toolbar>(R.id.cart_toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_home_back))
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        cartActivityViewModel.getListItems().observe(this,{
            cartRecyle.layoutManager = LinearLayoutManager(this)
            val adapter = CartAdapter(this,it as ArrayList<Cart>,cartActivityViewModel)
            cartRecyle.adapter = adapter
            for(item in it){
                total += item.price?.toInt()!!
            }
            txt_total_qty.text = "${it.size} item(s)"
            txt_total_amount.text = "Total: ₹$total/-"
        })



        btn_pay.setOnClickListener {
            createOrder()
        }
    }


    private fun createOrder() {

        retrofit.create(CheckoutInterface::class.java)
            .createOrder(
                Order(
                    total.toString().toInt()*100, "INR", "receipt2"
                )
            )
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CartActivity, "Success", Toast.LENGTH_SHORT)
                            .show()
                        val orderId = response.body()?.id.toString()
                        if (orderId.isNotEmpty()) {
                            startPayment(orderId)
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(this@CartActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startPayment(orderId: String) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        val activity: Activity = this
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Demoing Charges")
            options.put("order_id", orderId)
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", "${total*100}")
            val preFill = JSONObject()
            preFill.put("email", FirebaseAuth.getInstance().currentUser?.email)
            preFill.put("contact", "9971294004")
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }
    override fun onPaymentSuccess(p0: String?) {
        cartActivityViewModel.removeItem()
        startActivity(Intent(this,PaymentActivity::class.java).apply {
            putExtra("amount",total)
        })
        finish()
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Fail:$p1", Toast.LENGTH_SHORT).show()
    }
}