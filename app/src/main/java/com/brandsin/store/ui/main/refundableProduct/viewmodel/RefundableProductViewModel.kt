package com.brandsin.store.ui.main.refundableProduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.MessageResponse
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.main.refundableProduct.model.RefundableDetailsResponse
import com.brandsin.store.ui.main.refundableProduct.model.RefundableProductResponse
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.launch

class RefundableProductViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE

    private val _getAllRefundableProductResponse: MutableLiveData<ResponseHandler<RefundableProductResponse?>> =
        MutableLiveData()
    val getAllRefundableProductResponse: LiveData<ResponseHandler<RefundableProductResponse?>> =
        _getAllRefundableProductResponse.toSingleEvent()

    private val _getRefundableDetailsResponse: MutableLiveData<ResponseHandler<RefundableDetailsResponse?>> =
        MutableLiveData()
    val getRefundableDetailsResponse: LiveData<ResponseHandler<RefundableDetailsResponse?>> =
        _getRefundableDetailsResponse.toSingleEvent()

    private val _updateStatusRefundableProductResponse: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val updateStatusRefundableProductResponse: LiveData<ResponseHandler<MessageResponse?>> =
        _updateStatusRefundableProductResponse.toSingleEvent()

    var refundableId: Int? = null

    fun getAllRefundableProduct() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getAllRefundableProduct(
                    storeId = PrefMethods.getStoreData()?.id ?: 0,
                )
            }.collect {
                _getAllRefundableProductResponse.value = it
            }
        }
    }

    fun getRefundableDetailsByRefundableId() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getRefundableDetailsByRefundableId(
                    refundableId = refundableId,
                )
            }.collect {
                _getRefundableDetailsResponse.value = it
            }
        }
    }

    fun updateStatusRefundableProduct(status: String, note: String?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.updateStatusRefundableProduct(
                    refundableId = refundableId,
                    status = status,
                    note = note,
                )
            }.collect {
                _updateStatusRefundableProductResponse.value = it
            }
        }
    }
}