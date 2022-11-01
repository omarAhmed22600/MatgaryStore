package com.brandsin.store.ui.auth.condition

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.condition.ConditionsResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConditionViewModel : BaseViewModel()
{
    fun getConditions()
    {
        obsIsVisible.set(true)
        requestCall<ConditionsResponse?>({ withContext(Dispatchers.IO) {
            return@withContext getApiRepo().getConditions("terms_and_policy" , PrefMethods.getLanguage()) } })
        { res ->
            obsIsVisible.set(false)

            when (res!!.isSuccess) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message)
                }
            }
        }
    }

    init {
        getConditions()
    }
}