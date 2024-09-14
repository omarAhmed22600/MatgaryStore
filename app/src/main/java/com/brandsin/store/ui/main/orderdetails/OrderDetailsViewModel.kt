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

class OrderDetailsViewModel : BaseViewModel() {

    var orderContentsAdapter = OrderContentsAdapter()

    var orderDetails = OrderDetailsResponse()

    var obsSubTotal = ObservableField<Double>()
    var obsTotal = ObservableField<Double>()
    var obsDeliveryPrice = ObservableField<Double>()
    var obsPackagingPrice = ObservableField<Double>()
    var obsDiscountCoupon = ObservableField<Double>()
    var obsPaymentWay = ObservableField<String>()
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

    fun getOrderDetails2() {
        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<OrderDetailsResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getOrderDetails(
                    orderId,
                    PrefMethods.getLanguage()
                )
            }
        })
        { res ->
            when (res!!.success) {
                true -> {
                    obsIsLoading.set(false)
                    obsIsFull.set(true)

                    orderDetails = res
                    apiResponseLiveData.value = ApiResponse.success(res)

                    res.offers?.forEach {
                        val item = ItemsItem()
                        item.id = it.id
                        item.amount = it.amount
                        // item.product_description = it.offer!!.description
                        item.quantity = it.quantity ?: 0
                        item.skuCode = it.skuCode
                        item.productName = it.description ?: "" // it.offer?.name ?: ""
                        // item.userNotes = it.user_notes
                        item.type = it.type
                        item.image = it.image ?: "" // it.offer?.image ?: ""
                        // item.addons = null
                        item.storeId = null
                        res.items!!.add(item)
                    }

                    orderContentsAdapter.updateList(res.items as ArrayList<ItemsItem>)

                    isMapReady.value = true

                    obsPaymentWay.set(res.order?.billing?.gateway)
                    notifyChange()

                    obsUserName.set("${res.order?.user?.name} ${res.order?.user?.lastName}")
                    obsOrderNumber.set("${getString(R.string.order_number)}: ${res.order?.orderNumber}")

                    if (res.order?.store?.deliveryPrice.isNullOrEmpty()) {
                        obsDeliveryPrice.set(0.0)
                    } else {
                        // obsDeliveryPrice.set(res.order?.store?.deliveryPrice?.toDouble())
                        obsDeliveryPrice.set(res.shipping?.amount?.toDouble())
                    }

                    obsDiscountCoupon.set(res.payment?.discount)

                    if (res.order?.hasPackaging == 1) {
                        obsPackagingPrice.set(res.order.packagingPrice?.toDouble())
                    } else {
                        obsPackagingPrice.set(0.0)
                    }

                    obsSubTotal.set(res.total?.get(0)?.subTotal)

                    when {
                        res.total?.isEmpty() == true -> {
                            obsTotal.set(0.0)
                        }

                        else -> {
                            when (res.total?.get(0)?.total) {
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

                else -> {}
            }
        }
    }

    fun setUpdateStatusOrder(status: String) {
        request.orderId = orderId
        request.status = status

        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<UpdateStatusOrderResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().setUpdateStatusOrder(request)
            }
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

                else -> {}
            }
        }
    }

    fun onPrepareClicked() {
        setClickable()
        if (obsAcceptWay.get().isNullOrEmpty()) {
            obsAcceptWay.set(MyApp.context.getString(R.string.accept_with_delivering_order))
        }
        if (obsAcceptWay.get() == MyApp.context.getString(R.string.accept_with_delivering_order)) {
            setUpdateStatusOrder("accepted_with_delivery")
        } else if (obsAcceptWay.get() == MyApp.context.getString(R.string.accept_without_delivering_order)) {
            // setUpdateStatusOrder("accepted")
            setUpdateStatusOrder("accepted_without_delivery")
        }
    }

    /*fun onCancelClicked() {
        setClickable()
        setUpdateStatusOrder("rejected_by_store")
    }*/
}
