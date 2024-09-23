package com.brandsin.store.ui.main.products.products

import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brandsin.store.R
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.model.menu.commonquest.CommonQuesResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.ui.main.products.MyProductsFragment
import com.brandsin.store.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemProductViewModel(var item: ProductsItem) : ViewModel()
{
    var mutableLiveData: MutableLiveData<Any?> = SingleLiveEvent<Any?>()
    var obsPrice = ObservableField<String>()

    fun toggleStatus(v: View, onComplete: () -> Unit) {
        val viewModel = v.findFragment<MyProductsFragment>().viewModel
        viewModel.obsIsVisible.set(true)

        viewModel.requestCall<CommonQuesResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext viewModel.getApiRepo().changeProductStatus(item.id ?: -1)
            }
        }) { res ->
            viewModel.obsIsVisible.set(false)
            when (res!!.isSuccess) {
                true -> {
                    viewModel.apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    viewModel.apiResponseLiveData.value = ApiResponse.successMessage(
                        v.context.getString(R.string.success)
                    )
                }
            }

            if (item.status == "active")
            {
                item.status = "disabled"
            } else
            {
                item.status = "active"
            }

            // Call the callback when the operation is complete
            onComplete()
        }
    }

}