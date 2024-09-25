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
import com.brandsin.store.network.toRequestBodyParam
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.main.marketingRequest.MarketingRequest
import com.brandsin.store.ui.main.marketingRequest.model.PinStoriesMarketingResponse
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.mapToMutableLiveData
import com.brandsin.store.utils.toMultiPart
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class MarketingRequestViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE
    var pinType= MutableLiveData<String>("")
    var startDate= MutableLiveData("")
    var endDate= MutableLiveData("")
    var noOfDays= MutableLiveData("0")
    var showPlace= pinType.mapToMutableLiveData {
        when (it)
        {
           "story_to_home"  -> getString(R.string.pin_a_story_to_the_home_page)
            "story_to_offers" -> getString(R.string.pin_a_story_to_the_offers_page)
            "offer_to_home" -> getString(R.string.pin_an_offer_on_home_page)
            "offer_to_offers" -> getString(R.string.show_a_on_the_store_page)
            else -> ""
        }
    }
    val toHomeCardString = MutableLiveData("")
    val toOffersCardString = MutableLiveData("")
    val isArImageChanged = MutableLiveData(false)
    val isEnImageChanged = MutableLiveData(false)
    var arOfferImage: File? = null
    var enOfferImage: File? = null

    val isStoryType = pinType.map {
        it.startsWith("story")
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
    val storyToHomeCardRes = pinType.map {
        if (it == "story_to_home"||it == "offer_to_home") R.drawable.card_primary_stroke else R.drawable.card_transparent_stroke
    }
    val storyToOffersCardRes = pinType.map {
        if (it == "story_to_home"||it == "offer_to_home")R.drawable.card_transparent_stroke  else R.drawable.card_primary_stroke
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
    fun selectToOffers()
    {
        if (pinType.value!!.startsWith("story")) {
            pinType.value = "story_to_offers"
        } else {
            pinType.value = "offer_to_offers"
        }
        getPinStoriesMarketing()
    }
    fun selectToHome()
    {
        if (pinType.value!!.startsWith("story")) {
            pinType.value = "story_to_home"
        } else {
            pinType.value = "offer_to_home"
        }
        getPinStoriesMarketing()
    }

    fun getPinStoriesMarketing() {
        viewModelScope.launch {
            safeApiCall {
                val marketCode = when (pinType.value)
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
                val marketCode = when (pinType.value)
                {
                    "story_to_offers", "offer_to_offers" -> "offers"
                    "story_to_home", "offer_to_home" -> "home"
                    else -> ""
                }
                val context = when (pinType.value)
                {
                    "story_to_offers","story_to_home" -> "story"
                    "offer_to_offers","offer_to_home" -> "offer"
                    else -> ""
                }
                if (isStoryType.value == true) {
                    val storiesJson = storiesIds.joinToString(
                        prefix = "[\"",
                        separator = "\",\"",
                        postfix = "\"]"
                    )
                    apiInterface.createMarketingRequests(
                        context = context,
                        type = marketCode,
                        story = storiesJson,
                        numberOfShoppingDays = numberOfShoppingDays.toInt(),
                        startDate = startDate.value + " 11:00:00",
                        endDate = endDate.value + " 11:00:00",
                        price = pricePinStoriesType!!.toDouble(),
                    )
                } else {
                    if (enOfferImage == null)
                        enOfferImage = arOfferImage
                    apiInterface.createMarketingRequests(
                        context = context.toRequestBodyParam(),
                        type = marketCode.toRequestBodyParam(),
                        numberOfShoppingDays = numberOfShoppingDays.toInt().toRequestBodyParam(),
                        startDate = (startDate.value + " 11:00:00").toRequestBodyParam(),
                        endDate = (endDate.value + " 11:00:00").toRequestBodyParam(),
                        price = pricePinStoriesType!!.toDouble().toRequestBodyParam(),
                        enImage = arOfferImage!!.toMultiPart("image_ar"),
                        arImage = enOfferImage!!.toMultiPart("image_en"),
                    )
                }
            }.collect {
                _createMarketingRequestsResponse.value = it
            }
        }
    }
}