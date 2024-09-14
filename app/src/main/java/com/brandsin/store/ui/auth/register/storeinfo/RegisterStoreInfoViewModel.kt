package com.brandsin.store.ui.auth.register.storeinfo

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.StoreTagsItem
import com.brandsin.store.model.auth.StoreTagsRequest
import com.brandsin.store.model.auth.StoreTagsResponse
import com.brandsin.store.model.auth.StoreTypeItem
import com.brandsin.store.model.auth.StoreTypeResponse
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterStoreInfoViewModel : BaseViewModel() {
    var storeRequest = StoreRegister()
    var typesResponse = StoreTypeResponse()
    var typesList: ArrayList<StoreTypeItem> = ArrayList()

    var obsType = ObservableField<String>()

    private var storeTagsRequest = StoreTagsRequest()
    var tagsResponse = StoreTagsResponse()
    var tagsList: ArrayList<StoreTagsItem> = ArrayList()

    fun getStoreTypes() {
        requestCall<StoreTypeResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                    .getStoreTypes(PrefMethods.getLanguage())
            }
        })
        { res ->
            when (res!!.isSuccess) {
                true -> {
                    typesResponse = res
                    typesList = res.data as ArrayList<StoreTypeItem>
                    setValue(Codes.STORE_TYPE_SUCSSES)
                }

                else -> {}
            }
        }
    }

    fun getTags() {
        storeTagsRequest.category = storeRequest.categories
        storeTagsRequest.locale = PrefMethods.getLanguage()
        val baeRepo = BaseRepository()
        val responseCall: Call<StoreTagsResponse?> = baeRepo.apiInterface.getTags(storeTagsRequest)
        responseCall.enqueue(object : Callback<StoreTagsResponse?> {
            override fun onResponse(
                call: Call<StoreTagsResponse?>,
                response: Response<StoreTagsResponse?>
            ) {
                if (response.isSuccessful) {
                    when {
                        response.body()!!.success!! -> {
                            tagsResponse = response.body()!!
                            tagsList = response.body()!!.data as ArrayList<StoreTagsItem>
                            setValue(Codes.STORE_TAGS_SUCSSES)
                        }

                        else -> {
                            setValue(response.body()!!.message.toString())
                        }
                    }
                } else {
                    setValue(response.message())
                }
            }

            override fun onFailure(call: Call<StoreTagsResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }


    fun onSaveClicked() {
        when {
            storeRequest.categories == null || storeRequest.categories!!.isEmpty() -> {
                setValue(Codes.EMPTY_TYPE)
            }

            storeRequest.tags == null || storeRequest.tags!!.isEmpty() -> {
                setValue(Codes.EMPTY_TAGS)
            }

            storeRequest.storeName == null -> {
                setValue(Codes.EMPTY_STORE_NAME)
            }

            storeRequest.storeLat == null -> {
                setValue(Codes.EMPTY_STORE_LAT)
            }

            storeRequest.storeAddress == null -> {
                setValue(Codes.EMPTY_STORE_ADDRESS)
            }

            storeRequest.storeMinOrderPrice == null -> {
                setValue(Codes.EMPTY_STORE_MINIMUM)
            }

            storeRequest.storePhoneNumber == null -> {
                setValue(Codes.EMPTY_STORE_PHONE)
            }

            storeRequest.storePhoneNumber!!.length < 10 -> {
                setValue(Codes.INVALID_PHONE)
            }

            storeRequest.storeWhatsApp == null -> {
                setValue(Codes.EMPTY_STORE_WHATS)
            }

            storeRequest.storeWhatsApp!!.length < 10 -> {
                setValue(Codes.INVALID_STORE_WHATS)
            }

            storeRequest.checkDelivery == true && storeRequest.storeDeliveryPrice == null -> {
                setValue(Codes.EMPTY_STORE_PRICE)
            }

            storeRequest.checkDelivery == true && storeRequest.storeDeliveryDistance == null -> {
                setValue(Codes.EMPTY_STORE_Space)
            }

            storeRequest.checkDelivery == true && storeRequest.storeDeliveryTime == null -> {
                setValue(Codes.EMPTY_STORE_TIME)
            }

            storeRequest.storeImg == null -> {
                setValue(Codes.EMPTY_STORE_STORE_IMG)
            }

            storeRequest.storeThumb == null -> {
                setValue(Codes.EMPTY_STORE_STORE_THUMB)
            }

            storeRequest.checkCondition == false -> {
                setValue(Codes.FALSE_CHECK_CONDITION)
            }

            else -> {
                setValue(Codes.SUCCESS)
            }
        }
        setValue(Codes.ACCEPT_CLICKED)
    }

    fun onStoreImageClicked() {
        setValue(Codes.SELECT_STORE_IMAGE)
    }

    fun onChangeStoreImageClicked() {
        setValue(Codes.SELECT_STORE_IMAGE)
    }

    fun onStoreThumbClicked() {
        setValue(Codes.SELECT_STORE_THUMB)
    }

    fun onChangeStoreThumbClicked() {
        setValue(Codes.SELECT_STORE_THUMB)
    }

    fun onBackClicked() {
        setValue(Codes.BACK_PRESSED)
    }

    fun onLocationClicked() {
        setValue(Codes.LOCATION_CLICKED)
    }

    fun onTypeClicked() {
        if (typesList.isNotEmpty()) {
            setValue(Codes.STORE_CATEGORIES_CLICKED)
        } else {
            getStoreTypes()
        }
    }

    fun onTagsClicked() {
        if (storeRequest.categories == null || storeRequest.categories?.isEmpty() == true) {
            setValue(Codes.EMPTY_TYPE)
        } else {
            if (tagsList.isNotEmpty()) {
                setValue(Codes.STORE_TAGS_CLICKED)
            } else {
                getTags()
            }
        }
    }

    fun onConditionsClicked() {
        setValue(Codes.SHOW_CONDITIONS)
    }
}