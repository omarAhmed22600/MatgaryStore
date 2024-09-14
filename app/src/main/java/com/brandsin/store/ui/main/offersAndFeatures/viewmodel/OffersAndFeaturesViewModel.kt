package com.brandsin.store.ui.main.offersAndFeatures.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.main.offersAndFeatures.model.OfferAndFeatureDetailsResponse
import com.brandsin.store.ui.main.offersAndFeatures.model.OfferAndFeatureListResponse
import kotlinx.coroutines.launch

class OffersAndFeaturesViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE

    var offerAndFeatureId: Int? = null

    private val _getAllOffersAndFeaturesResponse: MutableLiveData<ResponseHandler<OfferAndFeatureListResponse?>> =
        MutableLiveData()
    val getAllOffersAndFeaturesResponse: LiveData<ResponseHandler<OfferAndFeatureListResponse?>> =
        _getAllOffersAndFeaturesResponse.toSingleEvent()

    private val _getOfferAndFeatureByIdResponse: MutableLiveData<ResponseHandler<OfferAndFeatureDetailsResponse?>> =
        MutableLiveData()
    val getOfferAndFeatureByIdResponse: LiveData<ResponseHandler<OfferAndFeatureDetailsResponse?>> =
        _getOfferAndFeatureByIdResponse.toSingleEvent()

    fun getAllOffersAndFeatures() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getAllOffersAndFeatures()
            }.collect {
                _getAllOffersAndFeaturesResponse.value = it
            }
        }
    }

    fun getOfferAndFeatureById() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getOfferAndFeatureById(offerAndFeatureId = offerAndFeatureId!!)
            }.collect {
                _getOfferAndFeatureByIdResponse.value = it
            }
        }
    }
}