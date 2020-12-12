package com.example.shopick.activities

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.HomeViewModel
import com.example.shopick.R
import com.example.shopick.adapters.StoresAdapter
import com.example.shopick.utils.gone
import com.example.shopick.utils.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.location_layout.view.*
import kotlinx.android.synthetic.main.stores_bottom_sheet.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    var storesList: ArrayList<String>? = null
    lateinit var homeViewModel: HomeViewModel
    private var sheetBehavior: BottomSheetBehavior<RelativeLayout>? = null
    private lateinit var mLocationManager: LocationManager
    private var check = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val rxPermissions = RxPermissions(this)
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        settings.setOnClickListener {
            val intent=Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        btn_shop.setOnClickListener {
            val intent = Intent(this, ShoppingList::class.java)
            startActivity(intent)
        }

        KeyboardVisibilityEvent.setEventListener(this) { isOpen ->
            if (isOpen) {
                choose_location_layout.gone()
                sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                choose_location_layout.visible()
            }
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getStoresList().observe(this, Observer {
            storesList = arrayListOf()
            storesList?.addAll(it)
            loadInAdapter(it as ArrayList<String>)
            Log.d("TAG", "onCreate: $storesList")
        })

        search_textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    storesList?.let { loadInAdapter(it) }
                } else {
                    val list = arrayListOf<String>()
                    for (item in storesList!!) {
                        if (item.contains(s.toString(), true))
                            list.add(item)
                    }
                    loadInAdapter(list)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Restricting start with space
                if (search_textInputEditText.text.toString()
                        .startsWith(" ")
                ) search_textInputEditText.setText(
                    ""
                )
            }

        })

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                loc_name.text = "Getting Location"
                startService(Intent(this, LocationService::class.java))
            }
        }

        choose_location.setOnClickListener {
            if (!check) {
                val builder = MaterialAlertDialogBuilder(this)
                val view = layoutInflater.inflate(R.layout.location_layout, null)
                builder.setView(view)
                val dialog = builder.create()
                view.findViewById<Button>(R.id.enable_location).setOnClickListener {
                    rxPermissions.request(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                        .subscribe { granted ->
                            if (granted) {
                                Log.d("Home", "onCreate: granted")
                                //If location services enabled then connect and fetch current location
                                if (mLocationManager.isProviderEnabled(
                                        LocationManager.GPS_PROVIDER
                                    )
                                ) {
                                    loc_name.text = "Getting Location"
                                    startService(Intent(this, LocationService::class.java))
//                                refreshing_location.visible()
//                                click_to_fetch.invisible()
                                    Log.d(localClassName, "onCreate: running")
                                    dialog.dismiss()
                                }
                            }
                        }
                }
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } else {
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    loc_name.text = "Getting Location"
                    startService(Intent(this, LocationService::class.java))
                }
            }
            setupFilterBottomSheet()
        }
    }

    private val mServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val locationIntent = Intent()
            if (intent?.getStringExtra("Area") != null) {
                intent.getStringExtra("Area")?.let {
                    locationIntent.putExtra("locationName", it)
                    loc_name.text = it
                    check = true
                    Log.d(localClassName, "onReceive: $it")
                }
            } else {
                intent?.getStringExtra("AdminArea").let {
                    locationIntent.putExtra("locationName", it)
                    loc_name.text = it
                    check = true
                    Log.d(localClassName, "onReceive: $it")
                }
            }
            locationIntent.putExtra("latitude", intent?.getDoubleExtra("Latitude", 0.0))
            locationIntent.putExtra("longitude", intent?.getDoubleExtra("Longitude", 0.0))
        }
    }


    private fun setupFilterBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(stores_bottom_sheet)

        sheetBehavior?.isHideable = false
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                BottomSheetBehavior.STATE_EXPANDED
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }

        })
    }

    private fun loadInAdapter(list: ArrayList<String>) {
        bottom_stores_list.setHasFixedSize(true)
        bottom_stores_list.layoutManager = LinearLayoutManager(this@HomeActivity)
        val adapter = StoresAdapter(list, this)
        bottom_stores_list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("com.example.shopick.user_location")
        registerReceiver(mServiceReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(mServiceReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}