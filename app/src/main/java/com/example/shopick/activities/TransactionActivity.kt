package com.example.shopick.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.CaptureAct
import com.example.shopick.CheckoutInterface
import com.example.shopick.R
import com.example.shopick.ShoppingListViewModel
import com.example.shopick.dagger.DaggerShopickComponent
import com.example.shopick.dagger.ShopickComponent
import com.example.shopick.datamodels.Item
import com.example.shopick.datamodels.Order
import com.example.shopick.datamodels.OrderResponse
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.zxing.integration.android.IntentIntegrator
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.recycler_list
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.UnsupportedEncodingException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import com.example.shopick.adapters.ListAdapter


class TransactionActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var checkout: Checkout
    private var sheetBehavior: BottomSheetBehavior<RelativeLayout>? = null
    private var listItems = ArrayList<String>()

    lateinit var shoppingListViewModel: ShoppingListViewModel
    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_checkout.visibility= View.VISIBLE

        shoppingListViewModel = ViewModelProvider(this@TransactionActivity).get(ShoppingListViewModel::class.java)

        Checkout.preload(applicationContext)

        checkout = Checkout()
        checkout.setKeyID("rzp_test_WsTEz4PgQowhnY")

        val component: ShopickComponent = DaggerShopickComponent.create()
        component.injectCheckout(this)

        btn_checkout.setOnClickListener {
//            createOrder()
            scanCode()
        }
        setupFilterBottomSheet()
    }

    private fun setupFilterBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(bottomsheet)

        sheetBehavior?.isHideable = false
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                BottomSheetBehavior.STATE_EXPANDED
                btn_checkout.visibility= View.INVISIBLE
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        shoppingListViewModel.getListItems().observe(this@TransactionActivity,{
                            recycler_list.setHasFixedSize(true)
                            recycler_list.layoutManager= LinearLayoutManager(this@TransactionActivity)
                            val adapter = ListAdapter(
                                it as ArrayList<Item>,
                                this@TransactionActivity,
                                shoppingListViewModel
                            )
                            recycler_list.adapter = adapter
                            btn_checkout.visibility= View.INVISIBLE
                        })

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        BottomSheetBehavior.STATE_COLLAPSED
                        btn_checkout.visibility= View.VISIBLE
                    }
                }
            }

        })
    }

    private fun scanCode() {
        IntentIntegrator(this).apply {
            captureActivity = CaptureAct::class.java
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Press volume up for flash\nPress volume down to close the flash")
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val intent = Intent(this, ScannedItemActivity::class.java)
                intent.putExtra("Barcode",result.contents.toString())
                startActivity(intent)
                Toast.makeText(this, result.contents.toString(), Toast.LENGTH_SHORT).show()
            }
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
                        Toast.makeText(this@TransactionActivity, "Success", Toast.LENGTH_SHORT)
                            .show()
                        val orderId = response.body()?.id.toString()
                        if (orderId.isNotEmpty()) {
                            startPayment(orderId)
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(this@TransactionActivity, "Fail", Toast.LENGTH_SHORT).show()
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