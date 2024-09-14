package com.brandsin.store.ui.menu.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.profile.addedstories.liststories.ListStoriesResponse
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.menu.wallet.model.WalletTransactionsResponse
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.launch

class WalletViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE

    init {
        obsIsLogin.set(true)
    }

    private val _getWalletTransactions: MutableLiveData<ResponseHandler<WalletTransactionsResponse?>> =
        MutableLiveData()
    val getWalletTransactions: LiveData<ResponseHandler<WalletTransactionsResponse?>> =
        _getWalletTransactions.toSingleEvent()

    fun getWalletTransactions() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getWalletTransactions(
                    limit = null,
                    page = null,
                    PrefMethods.getStoreData()?.id ?: 0
                )
            }.collect {
                _getWalletTransactions.value = it
            }
        }
    }
}