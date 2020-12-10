package com.example.shopick

import android.app.Activity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopick.dagger.DaggerShopickComponent
import com.example.shopick.dagger.ShopickComponent
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Credentials
import okhttp3.Request
import okhttp3.Route
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.UnsupportedEncodingException
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), PaymentResultListener{

    private lateinit var checkout: Checkout

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Checkout.preload(applicationContext)

        checkout = Checkout()
        checkout.setKeyID("rzp_test_WsTEz4PgQowhnY")

        val component: ShopickComponent = DaggerShopickComponent.create()
        component.injectCheckout(this)


        btn_checkout.setOnClickListener {
//            createOrder()
            startPayment()
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
                    5000, "INR", "receipt1"
                )
            )
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startPayment()  {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        val activity: Activity = this
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Demoing Charges")
            options.put("order_id", "order_GBE5rSS3Nk0r1u")
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

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
    }

    @Throws(UnsupportedEncodingException::class)
    fun getBase64String(value: String): String? {
        return Base64.encodeToString(value.toByteArray(charset("UTF-8")), Base64.DEFAULT)
    }

}