package com.brandsin.store.ui.profile.update

import android.util.Patterns
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.updateprofile.UpdateProfileRequest
import com.brandsin.store.model.profile.updateprofile.UpdateProfileResponse
import com.brandsin.store.utils.PrefMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileViewModel : BaseViewModel()
{
    var request = UpdateProfileRequest()

    init {
        if (PrefMethods.getUserData()!!.name!=null){
            request.name=PrefMethods.getUserData()!!.name
        }
        if (PrefMethods.getUserData()!!.lastName!=null){
            request.last_name=PrefMethods.getUserData()!!.lastName
        }
        if (PrefMethods.getUserData()!!.phoneNumber!=null){
            request.phone_number=PrefMethods.getUserData()!!.phoneNumber
        }
        if (PrefMethods.getUserData()!!.email!=null){
            request.email=PrefMethods.getUserData()!!.email
        }
    }
    fun onSaveClicked() {
        setClickable()
        when {
            request.name.isNullOrEmpty() || request.name.isNullOrBlank() -> {
                setValue(Codes.NAME_EMPTY)
            }
            request.last_name.isNullOrEmpty() || request.last_name.isNullOrBlank() -> {
                setValue(Codes.NAME_EMPTY)
            }
            request.phone_number.isNullOrEmpty() || request.phone_number.isNullOrBlank() -> {
                setValue(Codes.EMPTY_PHONE)
            }
            request.phone_number!!.length < 9 -> {
                setValue(Codes.INVALID_PHONE)
            }
            request.email.isNullOrEmpty() || request.email.isNullOrBlank() -> {
                setValue(Codes.EMAIL_EMPTY)
            }
            !Patterns.EMAIL_ADDRESS.matcher(request.email).matches() -> {
                setValue(Codes.EMAIL_INVALID)
            }
            else -> {
                setShowProgress(true)
                apiupdateProfile()
            }
        }
    }

    fun apiupdateProfile(){
        request.user_id = PrefMethods.getUserData()!!.id.toString()
        request.lang = PrefMethods.getLanguage()
        val baeRepo = BaseRepository()
        val responseCall: Call<UpdateProfileResponse?> = baeRepo.apiInterface.updateProfile(request)
        responseCall.enqueue(object : Callback<UpdateProfileResponse?> {
            override fun onResponse(call: Call<UpdateProfileResponse?>, response: Response<UpdateProfileResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        PrefMethods.saveUserData(response.body()!!.data)
                        setValue(Codes.EDIT_PROFILE)
                    }else{
                        setValue(response.body()!!.error.toString())
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<UpdateProfileResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
}