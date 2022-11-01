package com.brandsin.store.database

import androidx.lifecycle.MutableLiveData
import com.brandsin.user.database.RetrofitClient

open class BaseRepository
{
    var apiInterface: ApiInterface = RetrofitClient.getApiClient().create(ApiInterface::class.java)
    val mutableLiveData = MutableLiveData<Any?>()

    fun setValue(item: Any?) {
        mutableLiveData.value = item
        mutableLiveData.value = null
    }

}