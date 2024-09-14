package com.brandsin.store.ui.main.home

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.devicetoken.DeviceTokenRequest
import com.brandsin.store.model.auth.devicetoken.DeviceTokenResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.homepage.OldOrdersResponse
import com.brandsin.store.model.menu.connectingmain.list.ListConnectingMainResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class HomeViewModel : BaseViewModel() {

    var deviceTokenRequest = DeviceTokenRequest()

    private var obsOrdersSize = ObservableField<String>()

    var storeName = ""
    var requestId = -1

    init {
        getListConnectingMain()
        getNewOrders()
    }

    fun onNotificationsClicked() {
        setValue(Codes.NOTIFICATION_CLICK)
    }

    private fun getListConnectingMain() {
        val baeRepo = BaseRepository()
        val responseCall: Call<ListConnectingMainResponse?> = baeRepo.apiInterface
            .getListConnectingMain(PrefMethods.getStoreData()?.id ?: 0)
        responseCall.enqueue(object : Callback<ListConnectingMainResponse?> {
            override fun onResponse(
                call: Call<ListConnectingMainResponse?>,
                response: Response<ListConnectingMainResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        if (response.body()!!.data!!.isNotEmpty()) {
                            for (item in response.body()!!.data!!) {
                                if (item!!.accept == null || item.accept == 0) {
                                    storeName = item.store!!.name.toString()
                                    requestId = item.id!!.toInt()
                                    setValue(Codes.SHOW_Link)
                                    break
                                }
                            }
                        }
                    }
                } else {
                    setValue(response.message())
                }
            }

            override fun onFailure(call: Call<ListConnectingMainResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }

    /* Calling this api for getting new orders count */
    private fun getNewOrders() {
        requestCall<OldOrdersResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getStoreOrders(
                    PrefMethods.getLanguage(),
                    PrefMethods.getStoreData()?.id ?: 0,
                    limit = null, // 30
                    "new",
                    page = null
                )
            }
        })
        { res ->
            when (res!!.isSuccess) {
                true -> {
                    res.let {
                        when {
                            it.ordersList!!.isNotEmpty() -> {
                                when (it.ordersList.size) {
                                    0 -> {}

                                    else -> {
                                        obsOrdersSize.set(it.ordersList.size.toString())
                                    }
                                }
                            }

                            else -> {
                                obsIsEmpty.set(true)
                                obsIsFull.set(false)
                            }
                        }
                    }
                }

                else -> {
                    obsIsEmpty.set(true)
                    obsIsFull.set(false)
                }
            }
        }
    }

    fun deviceToken() {
        deviceTokenRequest.user_id = PrefMethods.getUserData()!!.id.toString()
        deviceTokenRequest.type = "android_token"

        val baeRepo = BaseRepository()
        val responseCall: Call<DeviceTokenResponse?> =
            baeRepo.apiInterface.deviceToken(deviceTokenRequest)
        responseCall.enqueue(object : Callback<DeviceTokenResponse?> {
            override fun onResponse(
                call: Call<DeviceTokenResponse?>,
                response: Response<DeviceTokenResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {

                    }
                } else {
                    setValue(response.message())
                }
            }

            override fun onFailure(call: Call<DeviceTokenResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
}