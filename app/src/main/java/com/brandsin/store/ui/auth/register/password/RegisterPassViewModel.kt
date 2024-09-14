package com.brandsin.store.ui.auth.register.password

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes

class RegisterPassViewModel : BaseViewModel() {

    var obsPassword = ObservableField<String>()
    var obsConfirmPassword = ObservableField<String>()

    fun onSaveClicked() {
        when {
            obsPassword.get() == null -> {
                setValue(Codes.PASSWORD_EMPTY)
            }

            obsPassword.get()!!.length < 6 -> {
                setValue(Codes.PASSWORD_SHORT)
            }

            obsConfirmPassword.get() == null -> {
                setValue(Codes.PASSWORD_EMPTY)
            }

            obsPassword.get() != obsConfirmPassword.get() -> {
                setValue(Codes.PASSWORD_NOT_MATCH)
            }

            else -> {
                setValue(Codes.SUCCESS)
            }
        }
    }

    fun onBackClicked() {
        setValue(Codes.BACK_PRESSED)
    }
}