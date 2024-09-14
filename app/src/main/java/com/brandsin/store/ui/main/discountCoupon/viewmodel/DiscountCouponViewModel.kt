package com.brandsin.store.ui.main.discountCoupon.viewmodel

import android.app.DatePickerDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.main.discountCoupon.model.CouponItem
import com.brandsin.store.ui.main.discountCoupon.model.CouponListResponse
import com.brandsin.store.ui.main.discountCoupon.model.CreateAndUpdateCouponResponse
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.launch
import java.util.Calendar

class DiscountCouponViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE

    var typeClicked = ""
    var couponItem: CouponItem = CouponItem()

    private val _getAllCouponsResponse: MutableLiveData<ResponseHandler<CouponListResponse?>> =
        MutableLiveData()
    val getAllCouponsResponse: LiveData<ResponseHandler<CouponListResponse?>> =
        _getAllCouponsResponse.toSingleEvent()

    private val _deleteCouponByIdResponse: MutableLiveData<ResponseHandler<CouponListResponse?>> =
        MutableLiveData()
    val deleteCouponByIdResponse: LiveData<ResponseHandler<CouponListResponse?>> =
        _deleteCouponByIdResponse.toSingleEvent()

    private val _createAndUpdateResponse: MutableLiveData<ResponseHandler<CreateAndUpdateCouponResponse?>> =
        MutableLiveData()
    val createAndUpdateResponse: LiveData<ResponseHandler<CreateAndUpdateCouponResponse?>> =
        _createAndUpdateResponse.toSingleEvent()

    fun getAllCoupons() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getAllCoupons(PrefMethods.getStoreData()?.id ?: 0)
            }.collect {
                _getAllCouponsResponse.value = it
            }
        }
    }

    fun deleteCouponById(couponId: Int) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.deleteCouponById(couponId)
            }.collect {
                _deleteCouponByIdResponse.value = it
            }
        }
    }

    fun createNewCoupon(
        code: String,
        type: String, // fixed, percentage
        value: String,
        start: String, expiry: String,
    ) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.createNewCoupon(
                    storeId = PrefMethods.getStoreData()?.id ?: 0,
                    code = code,
                    type = type, value = value, start = start, expiry = expiry,
                )
            }.collect {
                _createAndUpdateResponse.value = it
            }
        }
    }

    fun updateCouponByCouponId(
        couponId: Int,
        code: String,
        type: String, // fixed, percentage
        value: String?,
        start: String, expiry: String,
    ) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.updateCouponByCouponId(
                    couponId = couponId,
                    storeId = PrefMethods.getStoreData()?.id ?: 0,
                    code = code,
                    type = type, value = value, start = start, expiry = expiry,
                )
            }.collect {
                _createAndUpdateResponse.value = it
            }
        }
    }
}