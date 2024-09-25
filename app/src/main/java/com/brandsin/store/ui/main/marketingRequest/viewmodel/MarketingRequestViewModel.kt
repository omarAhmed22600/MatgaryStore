package com.brandsin.store.ui.main.marketingRequest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.brandsin.store.R
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.MessageResponse
import com.brandsin.store.model.Story
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.main.marketingRequest.model.PinStoriesMarketingResponse
import com.brandsin.store.utils.PrefMethods
import com.google.gson.Gson
import kotlinx.coroutines.launch
import timber.log.Timber

class MarketingRequestViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE
    var pinOffersType = MutableLiveData<String>(null)
    var pinStoriesType= MutableLiveData<String>(null)
    var startDate= MutableLiveData("")
    var endDate= MutableLiveData("")
    var noOfDays= MutableLiveData("0")
    var showPlace= pinStoriesType.map {
        when (it)
        {
           "story_to_home"  -> getString(R.string.pin_a_story_to_the_home_page)
            "story_to_offers" -> getString(R.string.pin_a_story_to_the_offers_page)
            else -> ""
        }
    }
    var pricePinStoriesType: String? = null
    var storiesIds: MutableList<String> = mutableListOf() // val story = listOf("2", "3")
    var storiesItem: ArrayList<Story> = ArrayList()


    private val _getListStoriesResponse: MutableLiveData<ResponseHandler<List<com.brandsin.store.model.ListStoriesResponse>?>> =
        MutableLiveData()
    val getListStoriesResponse: LiveData<ResponseHandler<List<com.brandsin.store.model.ListStoriesResponse>?>> =
        _getListStoriesResponse.toSingleEvent()

    private val _getPinStoriesMarketingResponse: MutableLiveData<ResponseHandler<PinStoriesMarketingResponse?>> =
        MutableLiveData()
    val getPinStoriesMarketingResponse: LiveData<ResponseHandler<PinStoriesMarketingResponse?>> =
        _getPinStoriesMarketingResponse.toSingleEvent()

    private val _createMarketingRequestsResponse: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val createMarketingRequestsResponse: LiveData<ResponseHandler<MessageResponse?>> =
         _createMarketingRequestsResponse.toSingleEvent()
    val storyToHomeCardRes = pinStoriesType.map {
        if (it == "story_to_home") R.drawable.card_primary_stroke else R.drawable.card_transparent_stroke
    }
    val storyToOffersCardRes = pinStoriesType.map {
        if (it == "story_to_home")R.drawable.card_transparent_stroke  else R.drawable.card_primary_stroke
    }

    fun getListStories() {
        viewModelScope.launch {
            safeApiCallOmar {
                // Make your API call here using Retrofit service or similar
                apiInterface.getNewListStories(PrefMethods.getStoreData()?.id ?: 0)
            }.collect {
                _getListStoriesResponse.value = it
            }
        }
    }
    fun selectStoryToOffers()
    {
        pinStoriesType.value = "story_to_offers"
        getPinStoriesMarketing()
    }
    fun selectStoryToHome()
    {
        pinStoriesType.value = "story_to_home"
        getPinStoriesMarketing()
    }

    fun getPinStoriesMarketing() {
        viewModelScope.launch {
            safeApiCall {
                val marketCode = when (pinStoriesType.value)
                {
                    "story_to_offers", "offer_to_offers" -> "offers"
                    "story_to_home", "offer_to_home" -> "home"
                    else -> {""}
                }
                // Make your API call here using Retrofit service or similar
                apiInterface.getPinStoriesMarketing(marketCode, PrefMethods.getLanguage())
            }.collect {
                _getPinStoriesMarketingResponse.value = it
            }
        }
    }

    fun createMarketingRequests(numberOfShoppingDays: String) {
//        val requestBody = CreateMarketingRequest()

        viewModelScope.launch {

            safeApiCall {
                val marketCode = when (pinStoriesType.value)
                {
                    "story_to_offers", "offer_to_offers" -> "offers"
                    "story_to_home", "offer_to_home" -> "home"
                    else -> ""
                }
                val context = when (pinStoriesType.value)
                {
                    "story_to_offers","story_to_home" -> "story"
                    "offer_to_offers","offer_to_home" -> "offer"
                    else -> ""
                }

                val storiesJson = storiesIds.joinToString(
                    prefix = "[\"",
                    separator = "\",\"",
                    postfix = "\"]"
                )
                Timber.e(storiesJson)
                // Make your API call here using Retrofit service or similar
                apiInterface.createMarketingRequests(
                    context = context,
                    type = marketCode,
                    story = storiesJson,
                    numberOfShoppingDays = numberOfShoppingDays.toInt(),
                    startDate = startDate.value+" 11:00:00",
                    endDate = endDate.value+" 11:00:00",
                    price = pricePinStoriesType!!.toDouble(),
                )
            }.collect {

                _createMarketingRequestsResponse.value = it

            }
        }
    }
}