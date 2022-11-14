package com.brandsin.store.ui.auth.register.personalinfo

import android.util.Patterns
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.register.UserRegister
import com.brandsin.store.model.constants.Codes

class RegisterPersonalInfoViewModel : BaseViewModel()
{
    var userData = UserRegister()

    fun onSaveClicked()
    {
        when {
            userData.userName == null -> {
                setValue(Codes.NAME_EMPTY)
            }
            userData.userLastName == null -> {
                setValue(Codes.EMPTY_LAST_NAME)
            }
            userData.userPhone == null -> {
                setValue(Codes.EMPTY_PHONE)
            }
            userData.userPhone!!.length < 10 -> {
                setValue(Codes.INVALID_PHONE)
            }
            userData.userEmail .isNullOrEmpty() ||  userData.userEmail .isNullOrBlank() -> {
                setValue(Codes.EMAIL_EMPTY)
            }
            !Patterns.EMAIL_ADDRESS.matcher(userData.userEmail).matches() -> {
                setValue(Codes.EMAIL_INVALID)
            }
            userData.userIdImg == null -> {
                setValue(Codes.EMPTY_IMAGE)
            }
            else -> {
                setValue(Codes.SUCCESS)
            }
        }
    }

    fun onImageClicked() {
        setValue(Codes.SELECT_IMAGES)
    }

    fun onChangeImageClicked() {
        setValue(Codes.SELECT_IMAGES)
    }

    fun onBackClicked()
    {
        setValue(Codes.BACK_PRESSED)
    }
}