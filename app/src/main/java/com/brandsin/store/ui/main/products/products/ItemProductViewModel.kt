package com.brandsin.store.ui.main.products.products

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.utils.SingleLiveEvent

class ItemProductViewModel(var item: ProductsItem) : ViewModel()
{
    var mutableLiveData: MutableLiveData<Any?> = SingleLiveEvent<Any?>()
    var obsPrice = ObservableField<String>()

}