package com.brandsin.store.ui.auth.register

import android.util.Log
import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.devicetoken.DeviceTokenRequest
import com.brandsin.store.model.auth.devicetoken.DeviceTokenResponse
import com.brandsin.store.model.auth.register.RegisterRequest
import com.brandsin.store.model.auth.register.RegisterResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : BaseViewModel() {

    var deviceTokenRequest = DeviceTokenRequest()
    var registerRequest = RegisterRequest()
    var obsIsPasswordEntered = ObservableField(false)
    var obsIsStoreInfoEntered = ObservableField(false)
    var obsIsPersonalInfoEntered = ObservableField(false)
    var obsIsBankingDataInfoEntered = ObservableField(false)

    fun onConditionsClicked() {
        setValue(Codes.SHOW_CONDITIONS)
    }

    fun onPersonalInfoClicked() {
        setValue(Codes.OPEN_PERSONAL_INFO_ACTIVITY)
    }

    fun onBankAccountInfoInfoClicked() {
        setValue(Codes.OPEN_BANK_ACCOUNT_INFO_ACTIVITY)
    }

    fun onStoreInfoClicked() {
        setValue(Codes.OPEN_STORE_INFO_ACTIVITY)
    }

    fun onPasswordClicked() {
        setValue(Codes.OPEN_PASSWORD_ACTIVITY)
    }

    fun onCreateStoreClicked() {
        when {
            obsIsStoreInfoEntered.get() == false -> {
                setValue(Codes.COMPLETE_STORE_DATA)
            }

            obsIsPersonalInfoEntered.get() == false -> {
                setValue(Codes.COMPLETE_USER_DATA)
            }

            obsIsBankingDataInfoEntered.get() == false -> {
                setValue(Codes.COMPLETE_BANKING_DATA)
            }

            obsIsPasswordEntered.get() == false -> {
                setValue(Codes.PASSWORD_EMPTY)
            }

            else -> {
                if (registerRequest.storeThumb == null) {
                    createAccount2()
                } else {
                    createAccount()
                }
            }
        }
    }

    private fun createAccount() {
        obsIsVisible.set(true)
        Log.d("createAccount", "registerRequest == $registerRequest")
        requestCall<RegisterResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().createNewStore(registerRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    PrefMethods.saveUserData(res.user)
                    PrefMethods.saveStoreData(res.store)
                    PrefMethods.saveLoginState(true)
                    deviceToken(res.user!!.id)
                    apiResponseLiveData.value = ApiResponse.success(res)
                }

                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }

    private fun createAccount2() {
        obsIsVisible.set(true)
        requestCall<RegisterResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().createNewStore(registerRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    PrefMethods.saveUserData(res.user)
                    PrefMethods.saveStoreData(res.store)
                    PrefMethods.saveLoginState(true)
                    deviceToken(res.user!!.id)
                    apiResponseLiveData.value = ApiResponse.success(res)
                }

                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }

    private fun deviceToken(id: Int?) {
        deviceTokenRequest.user_id = id.toString()
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