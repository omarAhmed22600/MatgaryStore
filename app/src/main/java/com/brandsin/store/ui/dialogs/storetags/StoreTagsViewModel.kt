package com.brandsin.store.ui.dialogs.storetags

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.StoreTagsItem
import com.brandsin.store.model.constants.Codes

class StoreTagsViewModel : BaseViewModel() {
    var tagsAdapter  = StoreTagsAdapter()
    var tagsList : ArrayList<StoreTagsItem> = ArrayList()

    fun onSaveClicked() {
        setValue(Codes.CONFIRM_CLICKED)
    }

    fun onCancelClicked() {
        setValue(Codes.CANCEL_CLICKED)
    }
}
