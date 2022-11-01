package com.brandsin.store.ui.activity.home

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.brandsin.store.R
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.menu.MenuBusyRequest
import com.brandsin.store.model.menu.MenuClosedRequest
import com.brandsin.store.model.menu.MenuResponse
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.PrefMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : BaseViewModel()
{
    val obsShowToolbar = ObservableBoolean()
    val obsTitle = ObservableField<String>()
    val obsUserName = ObservableField<String>()
    val obsBtnLogout = ObservableField<String>()
    private val obsLanguage = ObservableField<String>()

    var requestMenuBusy = MenuBusyRequest()
    var requestMenuClosed= MenuClosedRequest()

    fun setUpUserData()
    {
        obsIsLogin.set(true)
        obsUserName.set(MyApp.context.getString(R.string.name))
        obsBtnLogout.set(MyApp.context.getString(R.string.information_account))


        if (PrefMethods.getStoreData() == null) {
            obsIsLogin.set(false)
            obsUserName.set(MyApp.context.getString(R.string.welcome))
            obsBtnLogout.set(MyApp.context.getString(R.string.login))
        } else {
            obsIsLogin.set(true)
            obsBtnLogout.set(MyApp.context.getString(R.string.information_account))
            if (PrefMethods.getStoreData()!!.name.toString()!="null") {
                obsUserName.set(PrefMethods.getStoreData()!!.name.toString())
            }else{
                obsUserName.set(PrefMethods.getStoreData()!!.name.toString())
            }
        }
    }

    init
    {
        setUpUserData()
    }

    fun onLogoutClicked() {
        PrefMethods.saveLoginState(false)
        PrefMethods.deleteUserData()
        setValue(Codes.LOGOUT_CLICK)
    }

    fun onEditClicked() {
        if (PrefMethods.getStoreData() != null){
            setValue(Codes.EDIT_CLICKED)
        } else {
            setValue(Codes.BUTTON_LOGIN_CLICKED)
        }
    }

    fun setMenuBusy(i: Int) {
        requestMenuBusy.isBusy = i
        requestMenuBusy.storeId = PrefMethods.getStoreData()!!.id
        val baeRepo = BaseRepository()
        val responseCall: Call<MenuResponse?> = baeRepo.apiInterface.setMenuBusy(requestMenuBusy)
        responseCall.enqueue(object : Callback<MenuResponse?> {
            override fun onResponse(call: Call<MenuResponse?>, response: Response<MenuResponse?>) {
                if (response.isSuccessful) {
                    when {
                        response.body()!!.isSuccess!! -> {
                            PrefMethods.saveUserData(response.body()!!.user)
                            PrefMethods.saveStoreData(response.body()!!.store)
                            PrefMethods.saveLoginState(true)
                        }
                        else -> {
                            setValue(response.body()!!.message.toString())
                        }
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<MenuResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }

    fun setMenuClosed(i: Int) {
        requestMenuClosed.isClosed = i
        requestMenuClosed.storeId = PrefMethods.getStoreData()!!.id
        val baeRepo = BaseRepository()
        val responseCall: Call<MenuResponse?> = baeRepo.apiInterface.setMenuClosed(requestMenuClosed)
        responseCall.enqueue(object : Callback<MenuResponse?> {
            override fun onResponse(call: Call<MenuResponse?>, response: Response<MenuResponse?>) {
                if (response.isSuccessful) {
                    when {
                        response.body()!!.isSuccess!! -> {
                            PrefMethods.saveUserData(response.body()!!.user)
                            PrefMethods.saveStoreData(response.body()!!.store)
                            PrefMethods.saveLoginState(true)
                        }
                        else -> {
                            setValue(response.body()!!.message.toString())
                        }
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<MenuResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }

}