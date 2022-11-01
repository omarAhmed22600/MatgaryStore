package com.brandsin.store.ui.activity.map

import android.location.Geocoder
import android.location.Location
import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.constants.Const
import com.brandsin.store.utils.map.MapUtil

class MapViewModel : BaseViewModel()
{
    val obsAddress = ObservableField<String>()
    val searchedAddress = ObservableField<String>()
    var latitude =  Const.latitude
    var longitude = Const.longitude

    fun onBackClicked() {
        setValue(Codes.BACK_PRESSED)
    }

    fun onCurrentLocationClicked() {
        setValue(Codes.GETTING_CURRENT_LOCATION)
    }

    fun onSearchClicked() {
        setValue(Codes.SEARCH_LOCATION_CLICKED)
    }

    fun onSaveClicked() {
        setValue(Codes.CONFIRM_CLICKED)
    }

    fun onCloseClicked() {
        setValue(Codes.CLOSE_CLICKED)
    }

    fun gotLocation(location: Location, geocoder: Geocoder) {
        obsAddress.set(MapUtil.getLocationAddress(geocoder, location.latitude, location.longitude))
        latitude = location.latitude
        longitude = location.longitude
    }
}