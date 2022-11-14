package com.brandsin.store.ui.auth.login

import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.devicetoken.DeviceTokenRequest
import com.brandsin.store.model.auth.devicetoken.DeviceTokenResponse
import com.brandsin.store.model.auth.login.LoginRequest
import com.brandsin.store.model.auth.login.LoginResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.utils.PrefMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : BaseViewModel() {
    var deviceTokenRequest = DeviceTokenRequest()
    var request = LoginRequest()
    var code = ""
    var userID = ""

    fun onRegisterClicked() {
        setValue(Codes.REGISTER_INTENT)
    }

    fun onLoginClicked() {
        setClickable()

        when {
            request.email == null -> {
                setValue(Codes.EMPTY_PHONE)
            }
            request.email!!.length < 10 -> {
                setValue(Codes.INVALID_PHONE)
            }
            request.password.isNullOrEmpty() || request.password.isNullOrBlank() -> {
                setValue(Codes.PASSWORD_EMPTY)
            }
            request.password!!.length < 6 -> {
                setValue(Codes.PASSWORD_SHORT)
            }
            else -> {
                setShowProgress(true)
                apiLogin()
            }
        }
    }
    fun apiLogin(){
        val baeRepo = BaseRepository()
        val responseCall: Call<LoginResponse?> = baeRepo.apiInterface.login(request)
        responseCall.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                when {
                    response.isSuccessful -> {
                        when {
                            response.body()!!.isSuccess!! -> {
                                PrefMethods.saveUserData(response.body()!!.user)
                                PrefMethods.saveStoreData(response.body()!!.store)
                                PrefMethods.saveLoginState(true)
                                deviceToken()
                                setValue(Codes.LOGIN_SUCCESS)
                            }
                            else -> {
                                setValue(response.body()!!.message.toString())
                            }
                        }
                    }
                    else -> {
                        setValue(response.message())
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
    fun deviceToken(){

        deviceTokenRequest.user_id = PrefMethods.getUserData()!!.id.toString()
        deviceTokenRequest.type = "android_token"

        val baeRepo = BaseRepository()
        val responseCall: Call<DeviceTokenResponse?> = baeRepo.apiInterface.deviceToken(deviceTokenRequest)
        responseCall.enqueue(object : Callback<DeviceTokenResponse?> {
            override fun onResponse(call: Call<DeviceTokenResponse?>, response: Response<DeviceTokenResponse?>) {
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

    fun onForgotClicked() {
        setValue(Codes.FORGOT_INTENT)
    }
}