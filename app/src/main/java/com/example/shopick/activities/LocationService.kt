package com.example.shopick.activities

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*


class LocationService : Service() {

    val BROADCAST_ACTION = "com.example.shopick.user_location"
    //private val TWO_MINUTES = 1000 * 60 * 2
    var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    //var listener: MyLocationListener? = null
    //var previousBestLocation: Location? = null
    private var mGeocoder: Geocoder? = null

    var intent: Intent? = null
    //var counter = 0


    override fun onCreate() {
        super.onCreate()
        intent = Intent(BROADCAST_ACTION)
    }


    override fun onStart(intent: Intent, startId: Int) {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mGeocoder = Geocoder(this, Locale.getDefault())
        //listener = MyLocationListener()
        if (ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        } else {
            //locationManager?.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener as LocationListener, null)
            //mLocationManager?.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener as LocationListener, null)
            if (mLocationRequest == null) {
                mLocationRequest = LocationRequest.create()
                mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                mLocationRequest?.interval = 10000
                mLocationRequest?.numUpdates = 1
            }

            LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        val loc = locationResult?.lastLocation
                        loc?.latitude
                        loc?.longitude
                        var areaList: List<Address>? = null

                        try {
                            areaList = loc?.latitude?.let {
                                mGeocoder?.getFromLocation(it, loc.longitude, 1)
                            }
                            val locality = areaList?.get(0)
                            val area = locality?.subLocality
                            sendBroadCast(loc?.latitude, loc?.longitude, area,
                                locality?.adminArea)
                        }catch (e: Exception){

                        }
                    }

                }, Looper.myLooper())
        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun sendBroadCast(lat: Double?, long: Double?, area: String?, adminArea: String?) {
        intent?.putExtra("Latitude", lat)
        intent?.putExtra("Longitude", long)
        intent?.putExtra("Area", area)
        intent?.putExtra("AdminArea", adminArea)
        Log.d("LoactionService", "sendBroadCast: value sent $area,$adminArea")
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy()
        //mLocationManager?.removeUpdates(listener)
    }
}