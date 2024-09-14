package com.brandsin.store.ui.main.offers

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.constants.Const
import com.brandsin.store.model.main.offers.delete.DeleteOfferRequest
import com.brandsin.store.model.main.offers.delete.DeleteOfferResponse
import com.brandsin.store.model.main.offers.listoffer.OffersItemDetails
import com.brandsin.store.model.main.offers.listoffer.OffersResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OffersViewModel : BaseViewModel() {

    var offersAdapter = OffersAdapter()

    private lateinit var latitude: String
    private lateinit var longitude: String

    // private var isFirstTime = true

    var deleteOfferRequest = DeleteOfferRequest()

    fun getUserStatus() {
        getLatLng()
        getOffers()
        obsIsLogin.set(true)
    }

    /* init {
        when {
            isFirstTime -> {
                isFirstTime = false
                getUserStatus()
            }
        }
    }*/

    private fun getLatLng() {
        when {
            PrefMethods.getUserLocation() != null -> {
                when {
                    PrefMethods.getUserLocation()!!.address != null || PrefMethods.getUserLocation()!!.address != "null" -> {
                        latitude = PrefMethods.getUserLocation()!!.lat.toString()
                        longitude = PrefMethods.getUserLocation()!!.lng.toString()
                    }

                    else -> {
                        latitude = Const.latitude.toString()
                        longitude = Const.longitude.toString()
                    }
                }
            }

            else -> {
                latitude = Const.latitude.toString()
                longitude = Const.longitude.toString()
            }
        }
    }

    fun getOffers() {
        obsIsEmpty.set(false)
        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<OffersResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                    .getOffers(
                        PrefMethods.getStoreData()!!.id!!.toInt(),
                        50,
                        0,
                        PrefMethods.getLanguage()
                    )
            }
        })
        { res ->
            obsIsLoading.set(false)
            when (res!!.isSuccess) {
                true -> {
                    res.let {
                        if (it.offersList!!.isNotEmpty()) {
                            obsIsFull.set(true)
                            obsIsLoadingStores.set(false)
                            obsHideRecycler.set(true)
                            offersAdapter.updateList(res.offersList as ArrayList<OffersItemDetails>)
                        } else {
                            obsIsEmpty.set(true)
                            obsIsFull.set(false)
                        }
                    }
                }

                else -> {}
            }
        }
    }

    fun deleteOffer() {
        obsIsVisible.set(true)
        requestCall<DeleteOfferResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().deleteOffer(deleteOfferRequest)
            }
        })
        { res ->

            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }

                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }

    fun onAddOfferClicked() {
        setValue(Codes.ADD_OFFER)
    }

    fun onAddClicked() {
        setValue(Codes.ADD_OFFER)
    }
}