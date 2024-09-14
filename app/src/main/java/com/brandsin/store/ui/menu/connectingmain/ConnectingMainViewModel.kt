package com.brandsin.store.ui.menu.connectingmain

import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.menu.connectingmain.ConnectingMainRequest
import com.brandsin.store.model.menu.connectingmain.ConnectingMainResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConnectingMainViewModel  : BaseViewModel() {
    var request = ConnectingMainRequest()
    var txt = ""
    var type = ""

    fun getTxt2()
    {
        if (PrefMethods.getLanguage()== "ar"){
            type = "clone_products_ar"
        }else{
            type = "clone_products_en"
        }
        requestCall<PhoneNumberResponse?>({ withContext(Dispatchers.IO) { return@withContext getApiRepo().
        getPhoneNumber(type , PrefMethods.getLanguage()) } })
        { res ->
            when (res!!.isSuccess) {
                true -> {
                    txt = res.phoneNumber.toString()
                }

                else -> {}
            }
        }
    }
    fun onConnectClicked() {
        if (request.fromStoreId.isNullOrEmpty()){
            setValue(Codes.EMPTY_STORE_CODE)
        }else{
            setConnectingMain()
        }
    }
    fun setConnectingMain() {
        request.storeId = PrefMethods.getStoreData()!!.id
        val baeRepo = BaseRepository()
        val responseCall: Call<ConnectingMainResponse?> = baeRepo.apiInterface.setConnectingMain(request)
        responseCall.enqueue(object : Callback<ConnectingMainResponse?> {
            override fun onResponse(call: Call<ConnectingMainResponse?>, response: Response<ConnectingMainResponse?>) {
                if (response.isSuccessful) {
                    setValue(response.body()!!.success)
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<ConnectingMainResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
}