package com.brandsin.store.ui.menu.storecode

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoreCodeViewModel  : BaseViewModel() {
    var code = PrefMethods.getStoreData()!!.id.toString()
    var txt = ""
    var type = ""
    fun getTxt2()
    {
        if (PrefMethods.getLanguage()== "ar"){
            type = "store_code_ar"
        }else{
            type = "store_code_en"
        }
        requestCall<PhoneNumberResponse?>({ withContext(Dispatchers.IO) { return@withContext getApiRepo().
        getPhoneNumber(type , PrefMethods.getLanguage()) } })
        { res ->
            when (res?.isSuccess) {
                true -> {
                    txt = res.phoneNumber.toString()
                }

                else -> {}
            }
        }
    }
}