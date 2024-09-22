package com.brandsin.store.ui.menu.storeStatistics

import androidx.lifecycle.MutableLiveData
import com.brandsin.store.R
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoreStatisticsViewModel  : BaseViewModel() {
    val productList = listOf(
        Product("244", "تي شيرت حرف مع عظم السمك طب", "40 SR", R.drawable.app_logo),
        Product("244", "تي شيرت حرف مع عظم السمك طب", "40 SR", R.drawable.app_logo),
        Product("244", "تي شيرت حرف مع عظم السمك طب", "40 SR", R.drawable.app_logo),
        Product("244", "تي شيرت حرف مع عظم السمك طب", "40 SR", R.drawable.app_logo)
    )
    val showProducts = MutableLiveData(false)

    fun toggleShowProducts()
    {
        showProducts.value = showProducts.value!!.not()
    }

}