package com.brandsin.store.ui.profile.store

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.StoreTagsItem
import com.brandsin.store.model.auth.StoreTagsRequest
import com.brandsin.store.model.auth.StoreTagsResponse
import com.brandsin.store.model.auth.StoreTypeItem
import com.brandsin.store.model.auth.StoreTypeResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.updatestore.UpdateStoreRequest
import com.brandsin.store.model.profile.updatestore.UpdateStoreResponse
import com.brandsin.store.model.profile.updatestore.UploadRequest
import com.brandsin.store.model.profile.updatestore.UploadResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreInfoViewModel : BaseViewModel() {

    var updateRequest = UpdateStoreRequest()

    private var typesList: ArrayList<StoreTypeItem> = ArrayList()

    var typesResponse = StoreTypeResponse()

    var obsType = ObservableField<String>()

    private var storeTagsRequest = StoreTagsRequest()
    var tagsResponse = StoreTagsResponse()
    var tagsList: ArrayList<StoreTagsItem> = ArrayList()

    var checkDelivery = true
    var uploadRequest = UploadRequest()

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
                }

                else -> {}
            }
        }
    }

    fun getTags() {
        storeTagsRequest.category = updateRequest.categories
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
            updateRequest.categories == null || updateRequest.categories!!.isEmpty() -> {
                setValue(Codes.EMPTY_TYPE)
            }

            updateRequest.tags == null || updateRequest.tags!!.isEmpty() -> {
                setValue(Codes.EMPTY_TAGS)
            }

            updateRequest.storeName == null -> {
                setValue(Codes.EMPTY_STORE_NAME)
            }

            updateRequest.storeLat == null -> {
                setValue(Codes.EMPTY_STORE_LAT)
            }

            updateRequest.storeAddress == null -> {
                setValue(Codes.EMPTY_STORE_ADDRESS)
            }

            updateRequest.storeMinOrderPrice == null -> {
                setValue(Codes.EMPTY_STORE_MINIMUM)
            }

            updateRequest.storePhoneNumber == null -> {
                setValue(Codes.EMPTY_STORE_PHONE)
            }

            updateRequest.storePhoneNumber!!.length < 10 -> {
                setValue(Codes.INVALID_PHONE)
            }

            updateRequest.storeWhatsApp == null -> {
                setValue(Codes.EMPTY_STORE_WHATS)
            }

            updateRequest.storeWhatsApp!!.length < 10 -> {
                setValue(Codes.INVALID_STORE_WHATS)
            }

            checkDelivery && updateRequest.storeDeliveryPrice == null -> {
                setValue(Codes.EMPTY_STORE_PRICE)
            }

            checkDelivery && updateRequest.storeDeliveryDistance == null -> {
                setValue(Codes.EMPTY_STORE_Space)
            }

            checkDelivery && updateRequest.storeDeliveryTime == null -> {
                setValue(Codes.EMPTY_STORE_TIME)
            }
//            updateRequest.storeImg == null -> {
//                setValue(Codes.EMPTY_STORE_STORE_IMG)
//            }
//            updateRequest.storeThumb == null -> {
//                setValue(Codes.EMPTY_STORE_STORE_THUMB)
//            }
            else -> {
                updateStoreInfo()
            }
        }
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

    fun onPhoto1Clicked() {
        setValue(Codes.SELECT_PHOTO1)
    }

    fun onPhoto2Clicked() {
        setValue(Codes.SELECT_PHOTO2)
    }

    fun onPhoto3Clicked() {
        setValue(Codes.SELECT_PHOTO3)
    }

    fun onPhoto4Clicked() {
        setValue(Codes.SELECT_PHOTO4)
    }

    fun onPhoto5Clicked() {
        setValue(Codes.SELECT_PHOTO5)
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
        if (updateRequest.categories == null || updateRequest.categories!!.isEmpty()) {
            setValue(Codes.EMPTY_TYPE)
        } else {
            if (tagsList.isNotEmpty()) {
                setValue(Codes.STORE_TAGS_CLICKED)
            } else {
                getTags()
            }
        }
    }

    private fun updateStoreInfo() {
        obsIsVisible.set(true)
        println("updateRequest == $updateRequest")
        requestCall<UpdateStoreResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().updateStoreInfo(updateRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.isSuccess) {
                true -> {
                    PrefMethods.saveStoreData(res.store)
                    apiResponseLiveData.value = ApiResponse.success(res)
                }

                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message)
                }
            }
        }
    }

    fun upload(i: Int) {
        obsIsVisible.set(true)
        requestCall<UploadResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().upload(uploadRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    updateRequest.storeMedia!!.add(i, res.data!!.id!!)
                }

                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message)
                }
            }
        }
    }
}