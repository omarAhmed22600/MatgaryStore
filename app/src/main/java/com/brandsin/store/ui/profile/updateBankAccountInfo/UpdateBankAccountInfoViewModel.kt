package com.brandsin.store.ui.profile.updateBankAccountInfo

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.updatestore.UpdateStoreRequest
import com.brandsin.store.model.profile.updatestore.UpdateStoreResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateBankAccountInfoViewModel : BaseViewModel() {
    var updateRequest = UpdateStoreRequest()

    fun onSaveClicked() {
        when {
            else -> {
                updateBankAccountInfo()
            }
        }
    }

    fun onBackClicked() {
        setValue(Codes.BACK_PRESSED)
    }

    private fun updateBankAccountInfo() {
        obsIsVisible.set(true)
        requestCall<UpdateStoreResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().updateBankAccountInfo(updateRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.isSuccess) {
                true -> {
                    PrefMethods.saveStoreData(res.store)
                    apiResponseLiveData.value = ApiResponse.success(res)
                }

                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message)
                }
            }
        }
    }
}