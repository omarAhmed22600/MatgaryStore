package com.brandsin.store.utils.map

import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.brandsin.store.model.constants.Codes

class GpsUtils(private val context: Context)
{
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationRequest: LocationRequest = LocationRequest.create()

    // method for turn on GPS
    fun turnGPSOn(onGpsListener: onGpsListener?)
    {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            onGpsListener?.gpsStatus(true)
        }
        else
        {
            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                 //  GPS is already enable, callback GPS status through listener
                .addOnSuccessListener((context as Activity)) {
                    onGpsListener?.gpsStatus(true)
                }
                .addOnCompleteListener {
                }
                .addOnFailureListener(context) { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(context, Codes.GPS_SETTINGS_REQ_CODE)
                        } catch (sie: SendIntentException) {
                            // Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->{

                        }
                          //  var  errorMessage : String?= "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                    }
                }
                .addOnCanceledListener {
                }
        }
    }

    interface onGpsListener {
        fun gpsStatus(isGPSEnable: Boolean)
    }

    init
    {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = (2 * 1000).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
    }
}