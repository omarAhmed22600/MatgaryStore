package com.brandsin.store.ui.auth.register.bankAccountInfo

import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.StoreTagsItem
import com.brandsin.store.model.auth.StoreTagsRequest
import com.brandsin.store.model.auth.StoreTagsResponse
import com.brandsin.store.model.auth.StoreTypeItem
import com.brandsin.store.model.auth.StoreTypeResponse
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterBankAccountInfoViewModel : BaseViewModel() {
    var storeRequest = StoreRegister()

    fun onBackClicked() {
        setValue(Codes.BACK_PRESSED)
    }

    fun onSaveClicked() {
        when {
            storeRequest.storeOwnerName == null -> {
                setValue(Codes.EMPTY_OWNER_NAME)
            }

            storeRequest.storeBankName == null -> {
                setValue(Codes.EMPTY_BANK_NAME)
            }

            storeRequest.storeIban == null -> {
                setValue(Codes.EMPTY_IBAN)
            }

            else -> {
                setValue(Codes.SUCCESS)
            }
        }
        setValue(Codes.ACCEPT_CLICKED)
    }
}