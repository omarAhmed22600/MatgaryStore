package com.brandsin.store.utils.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber

object PermissionUtil
{
    fun isGranted(activity: Activity, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    fun requestPermission(activity: Activity, permissions: List<String>, ): MutableLiveData<AppPermissionResult> {
        val result = MutableLiveData<AppPermissionResult>()
        Dexter.withContext(activity)
            .withPermissions(permissions).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            result.postValue(AppPermissionResult.AllGood)
                        } else if (report.isAnyPermissionPermanentlyDenied) {
                            result.postValue(AppPermissionResult.OpenSettings)
                        } else {
                            result.postValue(AppPermissionResult.Fail)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            list: List<PermissionRequest>,
                            token: PermissionToken,
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        return result
    }

    fun getImagePermissions(): List<String> {
        val array: MutableList<String> = ArrayList()
        array.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        array.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        array.add(Manifest.permission.CAMERA)
        return array
    }

    fun getLocationPermissions(): List<String> {
        val array: MutableList<String> = ArrayList()
        array.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) array.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        return array
    }

    enum class AppPermissionResult {
        AllGood, OpenSettings, Fail
    }

    fun hasImagePermission(fragmentActivity: FragmentActivity): Boolean {
        return if (isGranted(fragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (isGranted(fragmentActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                isGranted(fragmentActivity, Manifest.permission.CAMERA)
            } else {
                false
            }
        } else {
            false
        }
    }

    fun hasLocationPermission(fragmentActivity: Activity): Boolean
    {
        return if (isGranted(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            if (isGranted(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    isGranted(fragmentActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else true
            }
            else
            {
                false
            }
        }
        else
        {
            false
        }
    }

    fun hasForgroundLocationPermission(fragmentActivity: Activity): Boolean
    {
        return if (isGranted(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            return  isGranted(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        else
        {
            false
        }
    }

    fun requestForgroundActivityPermission(fragment: Activity?, permissions: List<String?>?): MutableLiveData<AppPermissionResult>
    {
        val result = MutableLiveData<AppPermissionResult>()
        Dexter.withContext(fragment).withPermissions(permissions).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    Timber.e("locaaaation all good")
                    result.postValue(AppPermissionResult.AllGood)
                } else if (report.isAnyPermissionPermanentlyDenied) {
                    Timber.e("locaaaation open settings")
                    result.postValue(AppPermissionResult.OpenSettings)
                } else {
                    Timber.e("locaaaation failed")
                    result.postValue(AppPermissionResult.Fail)
                }
            }
            override fun onPermissionRationaleShouldBeShown(list: List<PermissionRequest>, token: PermissionToken) {
                Timber.e("locaaaation else")
                token.continuePermissionRequest()
            }
        }).check()
        return result
    }

    fun requestForgroundPermission(fragment: Fragment, permissions: List<String?>?): MutableLiveData<AppPermissionResult> {
        val result = MutableLiveData<AppPermissionResult>()
        Dexter.withContext(fragment.requireActivity()).withPermissions(permissions).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    Timber.e("locaaaation all good")
                    result.postValue(AppPermissionResult.AllGood)
                } else if (report.isAnyPermissionPermanentlyDenied) {
                    Timber.e("locaaaation open settings")
                    result.postValue(AppPermissionResult.OpenSettings)
                } else {
                    Timber.e("locaaaation failed")
                    result.postValue(AppPermissionResult.Fail)
                }
            }

            override fun onPermissionRationaleShouldBeShown(list: List<PermissionRequest>, token: PermissionToken) {
                Timber.e("locaaaation else")
                token.continuePermissionRequest()
            }
        }).check()
        return result
    }

    fun getForgroundLocationPermissions(): List<String> {
        val array: MutableList<String> = java.util.ArrayList()
        array.add(Manifest.permission.ACCESS_FINE_LOCATION)
        return array
    }
}