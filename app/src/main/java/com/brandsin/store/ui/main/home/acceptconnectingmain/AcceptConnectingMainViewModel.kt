package com.brandsin.store.ui.main.home.acceptconnectingmain

import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.menu.connectingmain.accept.AcceptConnectingMainRequest
import com.brandsin.store.model.menu.connectingmain.accept.AcceptConnectingMainResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcceptConnectingMainViewModel  : BaseViewModel() {
    var request = AcceptConnectingMainRequest()
    var txt = ""
    var type = ""
    var storeName = ""
    var requestId = -1

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
    fun onAgreeClicked() {
        setShowProgress(true)
        setAcceptConnectingMain(1)
    }

    fun onRefuseClicked() {
        setShowProgress(true)
        setAcceptConnectingMain(0)
    }
    fun setAcceptConnectingMain(i: Int) {
        request.requestId = requestId
        request.accept = i
        val baeRepo = BaseRepository()
        val responseCall: Call<AcceptConnectingMainResponse?> = baeRepo.apiInterface
            .setAcceptConnectingMain(request)
        responseCall.enqueue(object : Callback<AcceptConnectingMainResponse?> {
            override fun onResponse(call: Call<AcceptConnectingMainResponse?>, response: Response<AcceptConnectingMainResponse?>) {
                setShowProgress(false)
                if (response.isSuccessful) {
                    when (response.body()!!.success) {
                        true -> {
                            apiResponseLiveData.value = ApiResponse.success(response.body())
                        }
                        else -> {
                            apiResponseLiveData.value = ApiResponse.errorMessage(response.body()!!.message.toString())
                        }
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<AcceptConnectingMainResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
}