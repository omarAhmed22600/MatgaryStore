package com.brandsin.store.ui.profile.changepass

import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.changePass.ChangePassRequest
import com.brandsin.store.model.profile.changePass.ChangePassResponse
import com.brandsin.store.utils.PrefMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassViewModel : BaseViewModel()
{
    var request = ChangePassRequest()
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
            request.new_password.isNullOrEmpty() || request.new_password.isNullOrBlank() -> {
                setValue(Codes.PASSWORD_EMPTY)
            }
            request.new_password!!.length < 6 -> {
                setValue(Codes.PASSWORD_SHORT)
            }
            confirmPass.isEmpty() -> {
                setValue(Codes.CONFIRM_PASS_EMPTY)
            }
            confirmPass != request.new_password -> {
                setValue(Codes.PASSWORD_NOT_MATCH)
            }
            else -> {
                setShowProgress(true)
                apichangePass()
            }
        }
    }

    fun apichangePass(){
        request.user_id = PrefMethods.getUserData()!!.id.toString()
        request.lang = PrefMethods.getLanguage()
        val baeRepo = BaseRepository()
        val responseCall: Call<ChangePassResponse?> = baeRepo.apiInterface.changePass(request)
        responseCall.enqueue(object : Callback<ChangePassResponse?> {
            override fun onResponse(call: Call<ChangePassResponse?>, response: Response<ChangePassResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        setValue(Codes.CHANGE_PASS_SUCCESS)
                    }else{
                        setValue(response.body()!!.error.toString())
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<ChangePassResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
}