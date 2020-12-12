package com.example.shopick.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.shopick.CheckoutInterface
import com.example.shopick.R
import com.example.shopick.ShopickApplication
import com.example.shopick.dagger.DaggerShopickComponent
import com.example.shopick.dagger.ShopickComponent
import com.example.shopick.datamodels.Order
import com.example.shopick.datamodels.OrderResponse
import com.razorpay.Checkout
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class CartActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        ShopickApplication.getComponent().injectCheckout(this)

        val toolbar = findViewById<Toolbar>(R.id.cart_toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_home_back))
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_pay.setOnClickListener {
            createOrder()
        }
    }

    private fun createOrder() {

//        val auth = "Basic " + getBase64String("rzp_test_WsTEz4PgQowhnY:5EiTcDlRLRfgucU7lBfOxhqD");
//        Log.d("TAG", "createOrder: $auth")
//        val requestWithHeader: Request = Request.Builder()
//            .header("Authorization", Credentials.basic("YOUR_USER", "YOUR_PASS"))
//            .build()


        retrofit.create(CheckoutInterface::class.java)
            .createOrder(
                Order(
                    5000, "INR", "receipt2"
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
            options.put("amount", "100")
            val preFill = JSONObject()
            preFill.put("email", "test@razorpay.com")
            preFill.put("contact", "9876543210")
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }
}