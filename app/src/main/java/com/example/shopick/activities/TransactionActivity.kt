package com.example.shopick.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.CaptureAct
import com.example.shopick.CheckoutInterface
import com.example.shopick.R
import com.example.shopick.ShoppingListViewModel
import com.example.shopick.adapters.ListAdapter
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
import java.util.*
import javax.inject.Inject


class TransactionActivity : AppCompatActivity(){

    private lateinit var checkout: Checkout
    private var sheetBehavior: BottomSheetBehavior<RelativeLayout>? = null
    var tabOne: View? = null
    var tabTwo: View? = null
    var tabThree: View? = null
    private var tabOne_text: TextView? = null
    private var tabTwo_text: TextView? = null
    private var tabThree_text: TextView? = null

    lateinit var shoppingListViewModel: ShoppingListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTabIcons()

        val toolbar = findViewById<Toolbar>(R.id.transaction_toolbar)
        setSupportActionBar(toolbar)

        activity_title.text = intent?.getStringExtra("shop")
        activity_title_address.text = intent?.getStringExtra("address")

        shoppingListViewModel = ViewModelProvider(this@TransactionActivity).get(
            ShoppingListViewModel::class.java
        )

        Checkout.preload(applicationContext)

        checkout = Checkout()
        checkout.setKeyID("rzp_test_WsTEz4PgQowhnY")

        card_scan.setOnClickListener {
//            createOrder()
            scanCode()
        }
        setupFilterBottomSheet()
    }

    private fun setUpTabIcons() {
        tabOne = LayoutInflater.from(this)
            .inflate(R.layout.custom_tab, null)
        tabOne_text = tabOne?.findViewById(R.id.tab_Content)
        tabOne_text?.text = "Favourites"
        tabOne_text?.setTextColor(
            ContextCompat.getColorStateList(
                this,
                R.color.black
            )
        )
        tabLayout?.getTabAt(0)?.customView = tabOne

        tabTwo = LayoutInflater.from(this)
            .inflate(R.layout.custom_tab, null)
        tabTwo_text = tabTwo?.findViewById(R.id.tab_Content)
        tabTwo_text?.text = "Today's Offers"
        tabTwo_text?.setTextColor(
            ContextCompat.getColorStateList(
                this,
                R.color.black
            )
        )
        tabLayout?.getTabAt(1)?.customView = tabTwo

        tabThree = LayoutInflater.from(this)
            .inflate(R.layout.custom_tab, null)
        tabThree_text = tabThree?.findViewById(R.id.tab_Content)
        tabThree_text?.text = "Favourites"
        tabThree_text?.setTextColor(
            ContextCompat.getColorStateList(
                this,
                R.color.black
            )
        )
        tabLayout?.getTabAt(2)?.customView = tabThree

    }

    private fun setupFilterBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)

        sheetBehavior?.isHideable = false
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                BottomSheetBehavior.STATE_EXPANDED
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        shoppingListViewModel.getListItems().observe(this@TransactionActivity, {
                            recycler_list.setHasFixedSize(true)
                            recycler_list.layoutManager =
                                LinearLayoutManager(this@TransactionActivity)
                            val adapter = ListAdapter(
                                it as ArrayList<Item>,
                                this@TransactionActivity,
                                shoppingListViewModel
                            )
                            recycler_list.adapter = adapter
                        })

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        BottomSheetBehavior.STATE_COLLAPSED
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.cart_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cart) {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("shop",activity_title.text.toString())
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}