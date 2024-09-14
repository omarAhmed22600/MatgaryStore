package com.brandsin.store.ui.main.subscriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.launch

class SubscriptionViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE

    private val _getPlansSubscriptionResponse: MutableLiveData<ResponseHandler<SubscriptionListResponse?>> =
        MutableLiveData()
    val getPlansSubscriptionResponse: LiveData<ResponseHandler<SubscriptionListResponse?>> =
        _getPlansSubscriptionResponse.toSingleEvent()

    val subscriptionsList = ArrayList<SubscriptionPlansItem>()

    fun getPlansSubscription() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getPlansSubscription(
                    PrefMethods.getStoreData()?.id ?: 0,
                )
            }.collect {
                _getPlansSubscriptionResponse.value = it
            }
        }
    }

    fun addSubscriptionPlan(planId: Int) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.addSubscriptionPlan(
                    PrefMethods.getStoreData()?.id ?: 0,
                    planId
                )
            }.collect {
                // _getPlansSubscriptionResponse.value = it
            }
        }
    }

    fun changeSelectedSubscriptionPlan(
        subscriptionsList: List<SubscriptionPlansItem?>?,
        position: Int
    ): List<SubscriptionPlansItem?>? {
        subscriptionsList?.forEach { plan ->
            plan?.isSelected = false
        }
        subscriptionsList?.get(position)?.isSelected = true
        return subscriptionsList
    }
}