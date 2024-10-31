package com.brandsin.store.ui.menu.storeStatistics

import androidx.lifecycle.MutableLiveData
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.brandsin.store.R
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.main.products.list.ListProductsResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class StoreStatisticsViewModel  : BaseViewModel() {

    val productList = MutableLiveData<List<Product>>(listOf())
    val showProducts = MutableLiveData(false)
    val selectedYear = MutableLiveData("2024")
    val statisticsResponse = MutableLiveData<StatisticsResponse>()
    fun getData() {
        obsIsLoading.set(true)
        requestCall<StatisticsResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getStoreStatistics(
                    PrefMethods.getStoreData()!!.id!!.toInt(),
                    selectedYear.value.orEmpty().toInt(),
                    PrefMethods.getLanguage(),
                )
            }
        })
        { res ->
            obsIsLoading.set(false)
            res?.let {
                statisticsResponse.value = it
            } ?: run {
                Timber.e("Failed to fetch statistics: Response is null")
                // Optionally, handle the error state in the UI
            }
        }
    }
    fun toggleShowProducts()
    {
        showProducts.value = showProducts.value!!.not()
    }

}