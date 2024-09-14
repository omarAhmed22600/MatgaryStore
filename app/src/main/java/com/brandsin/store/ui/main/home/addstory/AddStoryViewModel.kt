package com.brandsin.store.ui.main.home.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.list.ListProductsResponse
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryRequest
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.requestCall
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddStoryViewModel : BaseViewModel() {

    var galleryAdapter = GalleryAdapter()
    var galleryList = ArrayList<String>()
    var request = UploadStoryRequest()

    var productName: String? = null
    var productSalePrice: String? = null

    var uploadType: String? = null

    val apiInterface = RetrofitBuilder.API_SERVICE

    private val _getStoreProductsListResponse: MutableLiveData<ResponseHandler<ListProductsResponse?>> =
        MutableLiveData()
    val getStoreProductsListResponse: LiveData<ResponseHandler<ListProductsResponse?>> =
        _getStoreProductsListResponse.toSingleEvent()

    fun onTextClicked() {
        setValue(Codes.SELECT_TEXT)
    }

    fun onImageClicked() {
        setValue(Codes.SELECT_IMAGES)
    }

    fun onVideoClicked() {
        setValue(Codes.SELECT_VIDEO)
    }

    fun uploadStories() {
        request.storeId = PrefMethods.getStoreData()!!.id!!.toInt()
        request.text = ""
        println("request: $request")
        requestCall<UploadStoryResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().uploadStories(request)
            }
        })
        { res ->
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

    fun getStoreProductsList() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getStoreProductsList(
                    storeId = PrefMethods.getStoreData()?.id ?: 0,
                    locale = PrefMethods.getLanguage(),
                    0,
                    50
                )
            }.collect {
                _getStoreProductsListResponse.value = it
            }
        }
    }

    fun changeSelectedProduct(
        productsList: ArrayList<ProductsItem?>?,
        position: Int
    ): ArrayList<ProductsItem?>? {
        productsList?.forEach { product ->
            product?.isSelected = false
        }
        productsList?.get(position)?.isSelected = true
        return productsList
    }
}