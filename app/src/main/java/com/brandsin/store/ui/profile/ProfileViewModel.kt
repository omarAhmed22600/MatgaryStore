package com.brandsin.store.ui.profile

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes

class ProfileViewModel : BaseViewModel() {

    fun onLanguageClicked() {
        setValue(Codes.LANGUAGE_CLICKED)
    }

    fun onProfileClicked() {
        setValue(Codes.EDIT_CLICKED)
    }

    fun onVehicleClicked() {
        setValue(Codes.VEHICLE_CLICKED)
    }

    fun onBankAccountInfoClicked() {
        setValue(Codes.EDIT_BANK_ACCOUNT_INFO_CLICKED)
    }

    fun onChangePassClicked() {
        setValue(Codes.CHANGE_PASS_CLICKED)
    }

    fun onAddStoriesClicked() {
        setValue(Codes.ADD_STORIES)
    }
}