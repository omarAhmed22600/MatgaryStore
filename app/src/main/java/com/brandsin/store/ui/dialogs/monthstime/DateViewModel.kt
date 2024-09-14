package com.brandsin.store.ui.dialogs.monthstime

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes

class DateViewModel : BaseViewModel() {

    fun onConfirmClicked() {
        setValue(Codes.CONFIRM_CLICKED)
    }
}