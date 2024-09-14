package com.brandsin.store.ui.activity.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityMapsBinding
import com.brandsin.store.model.UserLocation
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.constants.Const
import com.brandsin.store.ui.dialogs.permission.DialogPermissionFragment
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.map.GpsUtils
import com.brandsin.store.utils.map.MapUtil
import com.brandsin.store.model.constants.Params
import com.brandsin.user.utils.map.PermissionUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.widget.Autocomplete
import timber.log.Timber
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, Observer<Any?> {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapsBinding

    private lateinit var viewModel: MapViewModel

    private lateinit var geocoder: Geocoder
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    var from = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        binding.viewModel = viewModel

        from = intent.getIntExtra("from", -1)
        if (from == Codes.GETTING_USER_LOCATION) {
            binding.btnDeliverHere.text = getString(R.string.store_address)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.forLanguageTag(PrefMethods.getLanguage()))

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.mutableLiveData.observe(this, this)

        requestLocationPermission()
    }

    /* Checking the LOCATION permission state before asking the user runtime permission */
    private fun requestLocationPermission() {
        /* If user selected NEVER ASK AGAIN OR device policy prohibits the app from having that permission */
        when {
            PrefMethods.getIsPermissionDeniedForEver() -> {

                Utils.startDialogActivity(
                    this@MapsActivity,
                    DialogPermissionFragment::class.java.name,
                    Codes.OPEN_SETTING_DIALOG_REQUEST_CODE,
                    null
                )
            }
            /* If user clicked deny once Or this the first time to open the application */
            else -> {
                ActivityCompat.requestPermissions(
                    this@MapsActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Codes.ACCESS_LOCATION_REQUEST_CODE
                )
            }
        }
    }

    /* Handling actions when user click on Permissions dialog */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Codes.ACCESS_LOCATION_REQUEST_CODE -> {
                // Permission is granted. Continue the action or workflow
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        // Don't call tis method from here, I handle it (ON RESUME) method to not called twice
                        openLocationFromApp()
                    }

                    else -> {
                        when {
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this@MapsActivity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) -> {
                                // User clicked deny only once
                            }

                            else -> {
                                //Never ask again selected, or device policy prohibits the app from having that permission.
                                //So, disable that feature, or fall back to another situation...
                                PrefMethods.saveIsPermissionDeniedForEver(true)
                            }
                        }
                    }
                }
                return
            }
        }
    }

    /* When user clicked Allow to open GPS without going to setting page */
    private fun openLocationFromApp() {
        GpsUtils(this@MapsActivity).turnGPSOn(object : GpsUtils.onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                requestLocationUpdates()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val myLocation = LatLng(viewModel.latitude, viewModel.longitude)
        mMap.addMarker(MarkerOptions().position(myLocation))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, Const.zoomLevel))

        mMap.setOnMapClickListener { point: LatLng ->
            googleMap.clear()
            val userLocation = LatLng(point.latitude, point.longitude)
            viewModel.latitude = userLocation.latitude
            viewModel.longitude = userLocation.longitude
            val option =
                MarkerOptions().position(LatLng(userLocation.latitude, userLocation.longitude))
            mMap.addMarker(option)
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        userLocation.latitude,
                        userLocation.longitude
                    ), Const.zoomLevel
                )
            )
            viewModel.obsAddress.set(
                MapUtil.getLocationAddress(
                    getGeoCoder(),
                    userLocation.latitude,
                    userLocation.longitude
                )
            )
        }
    }

    override fun onChanged(value: Any?) {
        when (value) {
            null -> return
            else -> when (value) {

                /* When user click CURRENT LOCATION button ... Get current user location */
                Codes.GETTING_CURRENT_LOCATION -> {
                    when {
                        isPermissionGranted() -> {
                            openLocationFromApp()
                        }

                        else -> {
                            requestLocationPermission()
                        }
                    }
                }

                /* When User clicked the search toolbar */
                Codes.SEARCH_LOCATION_CLICKED -> {
                    when {
                        isPermissionGranted() -> {
                            MapUtil.openSearchPlaceScreen(this, value as Int)
                        }

                        else -> {
                            requestLocationPermission()
                        }
                    }
                }

                /* When user click confirm button ... Add this address item to Addresses list and finish the activity*/
                Codes.CONFIRM_CLICKED -> {
                    when (viewModel.latitude) {
                        /*
                        * If latitude and longitude is not selected before ... get current location
                        * and if location permission is not granted .. as user to allow it then turn gps on and get current user location
                        */
                        0.0 -> {
                            when {
                                isPermissionGranted() -> {
                                    requestLocationUpdates()
                                }

                                else -> {
                                    requestLocationPermission()
                                }
                            }
                        }
                        /* If location got save it and close activity */
                        else -> {
                            val userLocation = UserLocation()
                            userLocation.run {
                                lat = viewModel.latitude.toString()
                                lng = viewModel.longitude.toString()
                                address = MapUtil.getLocationAddress(
                                    getGeoCoder(),
                                    viewModel.latitude,
                                    viewModel.longitude
                                )
                            }
                            saveLocationAndCloseActivity(userLocation)
                        }
                    }
                }

                /* When user press back button of toolbar >>> Don't save any data, JUST finish the activity*/
                Codes.BACK_PRESSED -> {
                    val intent = Intent()
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                    setResult(Codes.GETTING_USER_LOCATION, intent)
                    finish()
                }

                Codes.CLOSE_CLICKED -> {
                    binding.mapDialog.visibility = View.GONE
                }
            }
        }
    }

    private fun saveLocationAndCloseActivity(userLocation: UserLocation) {
        val bundle = Bundle()
        bundle.putParcelable(Params.USER_LOCATION, userLocation)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Codes.GETTING_USER_LOCATION, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            /* Back from PLACES Search dialog */
            Codes.SEARCH_LOCATION_CLICKED -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        when {
                            place.latLng != null -> {
                                mMap.clear()
                                viewModel.latitude = place.latLng!!.latitude
                                viewModel.longitude = place.latLng!!.longitude
                                viewModel.obsAddress.set(
                                    MapUtil.getLocationAddress(
                                        getGeoCoder(),
                                        place.latLng!!.latitude,
                                        place.latLng!!.longitude
                                    )
                                )
                                mMap.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            place.latLng!!.latitude,
                                            place.latLng!!.longitude
                                        )
                                    )
                                )
                                mMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            place.latLng!!.latitude,
                                            place.latLng!!.longitude
                                        ), Const.zoomLevel
                                    )
                                )
                            }
                        }
                    }
                }
            }

            /* Back From Setting page */
            Codes.ALLOW_PERMISSION_FROM_SETTING_PAGE -> {
                when {
                    PermissionUtil.isGranted(
                        this@MapsActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) -> {
                        PrefMethods.saveIsPermissionDeniedForEver(false)
                        when {
                            isGpsEnabled() -> {
                                requestLocationUpdates()
                            }

                            else -> {
                                openLocationFromApp()
                            }
                        }
                    }
                }
            }

            /* When user clicked confirm to open setting page and allow permission from there */
            Codes.OPEN_SETTING_DIALOG_REQUEST_CODE -> {
                when {
                    data != null -> {
                        when {
                            data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                                when {
                                    data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 1 -> {
                                        openAppDetails()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        viewModel.isShown.set(true)
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = (2 * 1000).toLong()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    Timber.e("couldn't get location update")
                } else {
                    Timber.e("$locationResult")
                    if (locationResult.locations.size > 0) {
                        val location = locationResult.locations[0]

                        mMap.clear()
                        viewModel.gotLocation(location!!, getGeoCoder())
                        viewModel.latitude = location.latitude
                        viewModel.longitude = location.longitude
                        viewModel.obsAddress.set(
                            MapUtil.getLocationAddress(
                                getGeoCoder(),
                                location.latitude,
                                location.longitude
                            )
                        )
                        mMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                        )
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                ), Const.zoomLevel
                            )
                        )

                        stopLocationUpdates()
                    }
                }
                viewModel.isShown.set(false)
            }
        }
        try {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
        }
    }

    fun getGeoCoder(): Geocoder {
        return geocoder
    }

    /* Open app setting details page If user selected deny and don't ask again For the ACCESS_FINE_LOCATION permission */
    private fun openAppDetails() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, Codes.ALLOW_PERMISSION_FROM_SETTING_PAGE)
    }

    private fun isPermissionGranted(): Boolean {
        return when {
            !PermissionUtil.isGranted(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                false
            }

            else -> {
                true
            }
        }
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
        setResult(Codes.GETTING_USER_LOCATION, intent)
        finish()
    }
}