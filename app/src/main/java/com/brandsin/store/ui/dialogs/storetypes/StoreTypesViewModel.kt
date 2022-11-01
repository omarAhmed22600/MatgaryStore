package com.brandsin.store.ui.dialogs.storetypes

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.StoreTypeItem
import com.brandsin.store.model.constants.Codes

class StoreTypesViewModel : BaseViewModel()
{
    var typesAdapter  = StoreTypesAdapter()
    var typesList : ArrayList<StoreTypeItem> = ArrayList()

    fun onSaveClicked() {
        setValue(Codes.CONFIRM_CLICKED)
    }

    fun onCancelClicked() {
        setValue(Codes.CANCEL_CLICKED)
    }
}