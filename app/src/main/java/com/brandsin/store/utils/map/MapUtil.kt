package com.brandsin.store.utils.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.brandsin.store.R
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.utils.map.PermissionUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.IOException
import java.util.*

/**
 * Created by Mouaz Salah on 21/12/2020.
 **/

object MapUtil {

    /* Check That Device has Google play Store if its OS above of Android Lolipop*/
    @SuppressLint("ObsoleteSdkInt")
    fun hasAllLocationReq(activity: AppCompatActivity, liveData: MutableLiveData<Boolean>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (checkForGoogleApiAvailability(activity)) {
                Timber.e("----------- google api availability")
                checkForGpsEnabled(activity, liveData)
            }
        } else {
            Timber.e("----------- android version Under 5")
            if (!isGPSEnabled(activity))
                openGPSSetting(activity)
        }
    }

    // CHECK FOR LOCATION Requirements
    private fun checkForGpsEnabled(
        activity: AppCompatActivity,
        liveData: MutableLiveData<Boolean>,
    ) {
        val locationRequest =
            LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(0)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result =
            LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                // All location settings are satisfied. The client can initialize location requests here.
                if (!hasForegroundLocationPermissions(activity)) {
                    PermissionUtil.requestForgroundActivityPermission(
                        activity,
                        PermissionUtil.getForgroundLocationPermissions()
                    ).observe(activity) { appPermissionResult ->
                        when (appPermissionResult) {
                            PermissionUtil.AppPermissionResult.AllGood -> {
                                Timber.e("----------- All permissions all good")
                                liveData.setValue(true)
                            }
                            PermissionUtil.AppPermissionResult.OpenSettings -> {
                                Timber.e("----------- All permissions open setting")
                            }
                            PermissionUtil.AppPermissionResult.Fail -> {
                                Timber.e("----------- All permissions fail")
                            }
                            else -> {
                                Timber.e("----------- No permissions Get")

                                liveData.setValue(false)
                            }
                        }
                    }
                } else {
                    liveData.setValue(true)
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                activity,
                                Codes.CHECK_LOCATION_SETTINGS_REQUEST
                            )
                        } catch (ignore: IntentSender.SendIntentException) {
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->                         // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        openGPSSetting(activity)
                }
            }
        }
    }

    fun getLocationAddress(geocoder: Geocoder, latitude: Double, longitude: Double): String? {
        return try {
            val addresses: List<Address> =
                geocoder.getFromLocation(latitude, longitude, 1) ?: ArrayList()
            if (addresses.isNotEmpty()) {
                val fullAddress: String = addresses[0].getAddressLine(0)
                Timber.e(fullAddress)
                fullAddress
            } else null
        } catch (e: IOException) {
            Timber.e(e)
            null
        }
    }

    fun openSearchPlaceScreen(activity: Activity, requestCode: Int) {
        if (!Places.isInitialized()) {
            Places.initialize(
                MyApp.getInstance(),
                MyApp.getInstance().getString(R.string.google_maps_key),  /* Map API Key */
                Locale.forLanguageTag(PrefMethods.getLanguage()) /* lang Tag */
            )
        }
        val fields = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.NAME
        )
        val intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(activity)
        activity.startActivityForResult(intent, requestCode)
    }


    private fun checkForGoogleApiAvailability(activity: Activity): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val status =
            apiAvailability.isGooglePlayServicesAvailable(MyApp.getInstance())
        return if (status != ConnectionResult.SUCCESS) {
            if (!apiAvailability.isUserResolvableError(status)) {
                val snackBar = Snackbar.make(
                    activity.parent.currentFocus!!,
                    "Google Play Services unavailable. This app will not work",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackBar.setAction("close") { snackBar.dismiss() }
                snackBar.show()
                false
            } else if (status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                true
            } else {
                activity.shortToast("updated google play service + $status")
                false
            }
        } else {
            true
        }
    }

    private fun hasForegroundLocationPermissions(activity: Activity): Boolean {
        return PermissionUtil.isGranted(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        Timber.e(
            "----------- if hase fine location" + PermissionUtil.isGranted(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    /* private fun hasLocationPermissions(activity: Activity): Boolean {
         return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             PermissionUtil.isGranted(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                     && PermissionUtil.isGranted(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
         } else {
             PermissionUtil.isGranted(activity, Manifest.permission.ACCESS_FINE_LOCATION)
         }
     }*/

    /* Open Gps Setting page if GPS is closed*/
    private fun openGPSSetting(activity: AppCompatActivity) {
        val callGPSSettingIntent = Intent(
            Settings.ACTION_LOCATION_SOURCE_SETTINGS
        )
        activity.startActivityForResult(callGPSSettingIntent, Codes.GPS_SETTINGS_REQ_CODE)
    }

    private fun isGPSEnabled(context: Context): Boolean {
        val manager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}