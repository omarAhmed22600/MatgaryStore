package com.brandsin.store.ui.main.home.completedorders

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.homepage.OldOrdersResponse
import com.brandsin.store.model.main.homepage.StoreOrderItem
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompletedOrdersViewModel : BaseViewModel() {
    var completedOrdersAdapter = CompletedOrdersAdapter()

    init {
        getUserStatus()
    }

    fun getUserStatus() {
        getCompletedOrders()
        obsIsLogin.set(true)
        /*when {
            PrefMethods.getUserData() != null -> {
                obsIsLogin.set(true)
                getNotifications()
            }
            else -> {
                obsIsLogin.set(false)
            }
        }*/
    }

    fun onLoginClicked() {
        setValue(Codes.LOGIN_CLICKED)
    }

    private fun getCompletedOrders() {
        obsIsEmpty.set(false)
        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<OldOrdersResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getStoreOrders(
                    PrefMethods.getLanguage(),
                    PrefMethods.getStoreData()?.id ?: 0,
                    limit = null, // 30,
                    "old", page = null
                )
            }
        })
        { res ->

            obsIsLoading.set(false)
            when (res!!.isSuccess) {
                true -> {
                    res.let {
                        when {
                            it.ordersList!!.isNotEmpty() -> {
                                when (it.ordersList.size) {
                                    0 -> {
                                        obsIsFull.set(false)
                                        obsIsEmpty.set(true)
                                    }

                                    else -> {
                                        obsIsFull.set(true)
                                        obsIsEmpty.set(false)
                                        completedOrdersAdapter.updateList(it.ordersList as ArrayList<StoreOrderItem>)
                                    }
                                }
                            }

                            else -> {
                                obsIsEmpty.set(true)
                                obsIsFull.set(false)
                            }
                        }
                    }
                }

                else -> {
                    obsIsEmpty.set(true)
                    obsIsFull.set(false)
                }
            }
        }
    }

}