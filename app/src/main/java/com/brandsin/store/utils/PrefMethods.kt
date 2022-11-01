package com.brandsin.store.utils

import android.content.Context
import android.content.SharedPreferences

import androidx.core.content.edit
import com.google.gson.Gson
import com.brandsin.store.model.UserLocation
import com.brandsin.store.model.auth.register.StoreModel
import com.brandsin.store.model.auth.register.UserModel
import java.util.*
object Const{
    //"en" or "ar" or any as u want
    var DEFAULT_LANG: String = Locale.getDefault().language
    const val PREF_FILE_NAME = "HAGATY_USER_PREF"
    const val PREF_STORE_USER_DATA = "PREF_STORE_USER_DATA"
    const val PREF_STORE_MODEL = "PREF_STORE_MODEL"
    const val PREF_IS_LOGIN = "PREF_IS_LOGIN"
    const val PREF_ADDRESSES = "PREF_ADDRESSES"
    const val APP_PREF_NAME = "PREF_HAGATY_USER"
    const val PREF_LANG = "PREF_LANG"
    const val PREF_DEFAULT_ADDRESS = "PREF_DEFAULT_ADDRESS"
    const val PREF_USER_ADDRESS = "PREF_USER_ADDRESS"
    const val PREF_LOGIN_STATE = "PREF_LOGIN_STATE"
    const val PREF_COUNTRY_ID = "PREF_COUNTRY_ID"
    const val PREF_IS_PERMISSION_DENIED_FOR_EVER = "PREF_IS_PERMISSION_DENIED_FOR_EVER"
    const val PREF_IS_NOTIFICATIONS_ENABLED = "PREF_IS_NOTIFICATIONS_ENABLED"
    const val PREF_IS_ASKED_TO_LOGIN = "PREF_IS_ASKED_TO_LOGIN"
    const val PREF_Welcome = "PREF_Welcome"
}
 object PrefMethods {

    private var PRIVATE_MODE = 0

    private fun getSharedPreference(): SharedPreferences {
        val appCtx = MyApp.getInstance().applicationContext
        return appCtx.getSharedPreferences(Const.PREF_FILE_NAME, PRIVATE_MODE)
    }

    private fun getSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(Const.PREF_FILE_NAME, PRIVATE_MODE)
    }

    fun getLanguage(context: Context? = null): String {
        context?.let {
            return getSharedPreference(it).getString(Const.PREF_LANG,Const.DEFAULT_LANG)!!
        } ?: return getString(Const.PREF_LANG, Locale.getDefault().language)!!
    }

    fun setLanguage( value: String, context: Context?=null) {
        context?.let {
            getSharedPreference(it).edit {
                putString(Const.PREF_LANG, value)
            }
        }?: setString(Const.PREF_LANG, value)
    }


    /* THIS GETTER AND SETTER TO CHECK USER LOGIN STATE */
    fun getIsLogin(context: Context? = null): Boolean {
        context?.let {
            return getSharedPreference(it).getBoolean(Const.PREF_IS_LOGIN,false)
        } ?: return getBoolean(Const.PREF_IS_LOGIN, false)!!
    }

    fun setIsLogin( value: Boolean, context: Context? = null) {
        context?.let {
            getSharedPreference(it).edit {
                putBoolean(Const.PREF_IS_LOGIN, value)
            }
        }?: setBoolean(Const.PREF_IS_LOGIN, value)
    }

    fun saveUserData(data: UserModel?) {
        data?.let {
            val gSon = Gson()
            setString(Const.PREF_STORE_USER_DATA, gSon.toJson(it))
        }
    }

    fun getUserData(): UserModel? {
        getString(Const.PREF_STORE_USER_DATA, null)?.let {
            val gSon = Gson()
            return gSon.fromJson(it, UserModel::class.java)
        } ?: return null
    }

    fun saveStoreData(data: StoreModel?) {
        data?.let {
            val gSon = Gson()
            setString(Const.PREF_STORE_MODEL, gSon.toJson(it))
        }
    }

    fun getStoreData(): StoreModel? {
        getString(Const.PREF_STORE_MODEL, null)?.let {
            val gSon = Gson()
            return gSon.fromJson(it, StoreModel::class.java)
        } ?: return null
    }

    fun getLoginState(context: Context? = null): Boolean {
        context?.let {
            return getSharedPreference(it).getBoolean(Const.PREF_LOGIN_STATE,false)
        } ?: return getBoolean(Const.PREF_LOGIN_STATE, false)!!
    }

    fun saveLoginState( value: Boolean, context: Context? = null) {
        context?.let {
            getSharedPreference(it).edit {
                putBoolean(Const.PREF_LOGIN_STATE, value)
            }
        }?: setBoolean(Const.PREF_LOGIN_STATE, value)
    }

     fun getIsNotificationsEnabled(context: Context? = null): Boolean? {
         context?.let {
             return getSharedPreference(it).getBoolean(Const.PREF_IS_NOTIFICATIONS_ENABLED,true)
         } ?: return getBoolean(Const.PREF_IS_NOTIFICATIONS_ENABLED, true)
     }

     fun setIsNotificationsEnabled( value: Boolean,context: Context? = null) {
         context?.let {
             getSharedPreference(it).edit {
                 putBoolean(Const.PREF_IS_NOTIFICATIONS_ENABLED, value)
             }
         }?: setBoolean(Const.PREF_IS_NOTIFICATIONS_ENABLED, value)
     }


    /* ------ Deleting Cash --------  */
    fun deleteUserData() {
        remove(Const.PREF_STORE_USER_DATA)
        remove(Const.PREF_STORE_MODEL)
    }

    private fun remove(key: String) {
        getSharedPreference().edit { remove(key) }
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return getSharedPreference().getString(key, defaultValue)
    }

    private fun setString(key: String, value: String) {
        getSharedPreference().edit { putString(key, value) }
    }

    private fun getBoolean(key: String, defaultValue: Boolean? = null): Boolean? {
        return getSharedPreference().getBoolean(key, defaultValue!!)
    }

    private fun setBoolean(key: String, value: Boolean) {
        getSharedPreference().edit { putBoolean(key, value) }
    }

    fun saveIsPermissionDeniedForEver( value: Boolean, context: Context? = null) {
        context?.let {
            getSharedPreference(it).edit {
                putBoolean(Const.PREF_IS_PERMISSION_DENIED_FOR_EVER, value)
            }
        }?: setBoolean(Const.PREF_IS_PERMISSION_DENIED_FOR_EVER, value)
    }

    fun getIsPermissionDeniedForEver(context: Context? = null): Boolean {
        context?.let {
            return getSharedPreference(it).getBoolean(Const.PREF_IS_PERMISSION_DENIED_FOR_EVER,false)
        } ?: return getBoolean(Const.PREF_IS_PERMISSION_DENIED_FOR_EVER, false)!!
    }

    fun getIsAskedToLogin(context: Context? = null): Boolean {
        context?.let {
            return getSharedPreference(it).getBoolean(Const.PREF_IS_ASKED_TO_LOGIN,false)
        } ?: return getBoolean(Const.PREF_IS_ASKED_TO_LOGIN, false)!!
    }

    fun saveIsAskedToLogin( value: Boolean, context: Context? = null) {
        context?.let {
            getSharedPreference(it).edit {
                putBoolean(Const.PREF_IS_ASKED_TO_LOGIN, value)
            }
        }?: setBoolean(Const.PREF_IS_ASKED_TO_LOGIN, value)
    }

    fun saveUserLocation(data: UserLocation?) {
        data?.let {
            val gSon = Gson()
            setString(Const.PREF_USER_ADDRESS, gSon.toJson(it))
        }
    }

    fun getUserLocation(): UserLocation? {
        getString(Const.PREF_USER_ADDRESS, null)?.let {
            val gSon = Gson()
            return gSon.fromJson(it, UserLocation::class.java)
        } ?: return null
    }

     fun getWelcome(context: Context? = null): Boolean {
         context?.let {
             return getSharedPreference(it).getBoolean(Const.PREF_Welcome,false)
         } ?: return getBoolean(Const.PREF_Welcome, false)!!
     }

     fun saveWelcome( value: Boolean, context: Context? = null) {
         context?.let {
             getSharedPreference(it).edit {
                 putBoolean(Const.PREF_Welcome, value)
             }
         }?: setBoolean(Const.PREF_Welcome, value)
     }
}