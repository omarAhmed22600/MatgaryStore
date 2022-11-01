package com.brandsin.store.ui.main.orderdetails

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.brandsin.store.R
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.homepage.ItemsItem
import com.brandsin.store.model.main.homepage.OrderDetailsResponse
import com.brandsin.store.model.main.order.updatestatus.UpdateStatusOrderRequest
import com.brandsin.store.model.main.order.updatestatus.UpdateStatusOrderResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class OrderDetailsViewModel : BaseViewModel()
{
    var orderContentsAdapter  = OrderContentsAdapter()
    var orderDetails = OrderDetailsResponse()
    var obsTotal  = ObservableField<Double>()
    var obsDeliveryPrice= ObservableField<Double>()
    var obsPaymentWay  = ObservableField<String>()
    var isMapReady = MutableLiveData<Boolean>()
    var obsAcceptWay = ObservableField<String>()
    var obsUserName = ObservableField<String>()
    var obsOrderNumber = ObservableField<String>()

    var orderId = 0
    var request = UpdateStatusOrderRequest()

    fun onCallTextClicked() {
        setValue(Codes.CALL_CLICKED)
    }

    fun onCallImgClicked() {
        setValue(Codes.CALL_CLICKED)
    }

    fun getOrderDetails2()
    {
        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<OrderDetailsResponse?>({
            withContext(Dispatchers.IO) { return@withContext getApiRepo().getOrderDetails(orderId, PrefMethods.getLanguage()) } })
        { res ->
            when (res!!.success) {
                true -> {
                    obsIsLoading.set(false)
                    obsIsFull.set(true)

                    apiResponseLiveData.value = ApiResponse.success(res)

                    orderContentsAdapter.updateList(res.items as ArrayList<ItemsItem>)
                    orderDetails = res
                    isMapReady.value = true
                    obsPaymentWay.set(res.order!!.billing!!.gateway)
                    notifyChange()
                    obsUserName.set("${res.order.firstName} ${res.order.lastName}")
                    obsOrderNumber.set("${getString(R.string.order_number)}: ${res.order.orderNumber}")

                    if (res.order.store!!.deliveryPrice.isNullOrEmpty()){
                        obsDeliveryPrice.set(0.0)
                    }else{
                        obsDeliveryPrice.set(res.order.store.deliveryPrice!!.toDouble())
                    }

                    when {
                        res.total!!.isEmpty() -> {
                            obsTotal.set(0.0)
                        }
                        else -> {
                            when (res.total[0]!!.total) {
                                null -> {
                                    obsTotal.set(0.0)
                                }
                                else -> {
                                    obsTotal.set(res.total[0]!!.total!!.toDouble())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setUpdateStatusOrder(status: String) {
        request.order_id = orderId
        request.status = status

        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<UpdateStatusOrderResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().setUpdateStatusOrder(request) }
        })
        { res ->
            obsIsLoading.set(false)
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                false -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message)
                }
            }
        }
    }

    fun onPrepareClicked() {
        setClickable()
        if (obsAcceptWay.get().isNullOrEmpty()){
            obsAcceptWay.set(MyApp.context.getString(R.string.accept_with_delivering_order))
        }
        if (obsAcceptWay.get() == MyApp.context.getString(R.string.accept_with_delivering_order)) {
            setUpdateStatusOrder("accepted_with_delivery")
        }else if (obsAcceptWay.get() == MyApp.context.getString(R.string.accept_without_delivering_order)) {
            setUpdateStatusOrder("accepted")
        }
    }

    fun onCancelClicked() {
        setClickable()
        setUpdateStatusOrder("rejected_by_store")
    }
}
