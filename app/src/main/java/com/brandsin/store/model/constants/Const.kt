package com.brandsin.store.model.constants

import java.util.*

object Const
{
    const val latitude =  30.080027
    const val longitude = 31.3276914
    const val zoomLevel = 14.0f // This goes up to 21
    var DEFAULT_LANG: String = Locale.getDefault().language
    const val APP_PREF_NAME = "PREF_HAGATY_USER"
    const val ACCESS_LOGIN = "ACCESS_LOGIN"
    const val PREF_LANG = "PREF_LANG"
    const val PREF_USER_DATA = "PREF_USER_DATA"
    const val PREF_USER_ADDRESS = "PREF_USER_ADDRESS"
    const val PREF_LOGIN_STATE = "PREF_LOGIN_STATE"
    const val PREF_ADDRESSES = "PREF_ADDRESSES"
    const val PREF_CART_LIST = "PREF_CART_LIST"
    const val PREF_CART_DATA = "PREF_CART_DATA"
    const val PREF_COUNTRY_ID = "PREF_COUNTRY_ID"
    const val PREF_IS_PERMISSION_DENIED_FOR_EVER = "PREF_IS_PERMISSION_DENIED_FOR_EVER"

    const val DEVICE_TYPE = "Android"
}