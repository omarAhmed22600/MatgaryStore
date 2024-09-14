package com.brandsin.store.ui.main.marketingRequest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.MessageResponse
import com.brandsin.store.model.profile.addedstories.liststories.ListStoriesResponse
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.main.marketingRequest.model.PinStoriesMarketingResponse
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.launch

class MarketingRequestViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE

    var pinStoriesType: String? = null
    var pricePinStoriesType: String? = null
    var storiesIds: MutableList<String> = mutableListOf() // val story = listOf("2", "3")
    var storiesItem: ArrayList<StoriesItem> = ArrayList()

    private val _getListStoriesResponse: MutableLiveData<ResponseHandler<ListStoriesResponse?>> =
        MutableLiveData()
    val getListStoriesResponse: LiveData<ResponseHandler<ListStoriesResponse?>> =
        _getListStoriesResponse.toSingleEvent()

    private val _getPinStoriesMarketingResponse: MutableLiveData<ResponseHandler<PinStoriesMarketingResponse?>> =
        MutableLiveData()
    val getPinStoriesMarketingResponse: LiveData<ResponseHandler<PinStoriesMarketingResponse?>> =
        _getPinStoriesMarketingResponse.toSingleEvent()

    private val _createMarketingRequestsResponse: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val createMarketingRequestsResponse: LiveData<ResponseHandler<MessageResponse?>> =
        _createMarketingRequestsResponse.toSingleEvent()

    fun getListStories() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getNewListStories(PrefMethods.getStoreData()?.id ?: 0)
            }.collect {
                _getListStoriesResponse.value = it
            }
        }
    }

    fun getPinStoriesMarketing() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getPinStoriesMarketing(pinStoriesType!!, PrefMethods.getLanguage())
            }.collect {
                _getPinStoriesMarketingResponse.value = it
            }
        }
    }

    fun createMarketingRequests(numberOfShoppingDays: String) {
//        val requestBody = CreateMarketingRequest()

        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.createMarketingRequests(
                    type = pinStoriesType!!,
                    story = storiesIds,
                    numberOfShoppingDays = numberOfShoppingDays.toInt(),
                    price = pricePinStoriesType!!.toDouble(),
                )
            }.collect {
                // _createMarketingRequestsResponse.value = it
            }
        }
    }
}