package com.brandsin.store.ui.main.home.neworders

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.homepage.OldOrdersResponse
import com.brandsin.store.model.main.homepage.StoreOrderItem
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class NewOrdersViewModel : BaseViewModel()
{
    var newOrdersAdapter = NewOrdersAdapter()

    init
    {
        getUserStatus()
    }

    fun getUserStatus() {
        getNewOrders()
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

    private fun getNewOrders()
    {
        obsIsEmpty.set(false)
        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<OldOrdersResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getStoreOrders(PrefMethods.getLanguage() , PrefMethods.getStoreData()!!.id!!.toInt()  , 30,"new" , 1)
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
                                        newOrdersAdapter.updateList(it.ordersList as ArrayList<StoreOrderItem>)
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