package com.brandsin.store.ui.auth.resetpass

import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.resetpass.ResetPassRequest
import com.brandsin.store.model.auth.resetpass.ResetPassResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.utils.PrefMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetViewModel : BaseViewModel()
{
    var request = ResetPassRequest()
    var confirmPass=""

    fun onSaveClicked() {
        setClickable()
        when {
            request.password.isNullOrEmpty() || request.password.isNullOrBlank() -> {
                setValue(Codes.PASSWORD_EMPTY)
            }
            request.password!!.length < 6 -> {
                setValue(Codes.PASSWORD_SHORT)
            }
            confirmPass.isEmpty() -> {
                setValue(Codes.CONFIRM_PASS_EMPTY)
            }
            confirmPass != request.password -> {
                setValue(Codes.PASSWORD_NOT_MATCH)
            }
            else -> {
                setShowProgress(true)
                apiResetPass()
            }
        }
    }

    fun apiResetPass(){

        request.lang= PrefMethods.getLanguage()
        val baeRepo = BaseRepository()
        val responseCall: Call<ResetPassResponse?> = baeRepo.apiInterface.resetPass(request)
        responseCall.enqueue(object : Callback<ResetPassResponse?> {
            override fun onResponse(call: Call<ResetPassResponse?>, response: Response<ResetPassResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        setValue(Codes.RESET_SUCCESS)
                    }else{
                        setValue(response.body()!!.error.toString())
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<ResetPassResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
}