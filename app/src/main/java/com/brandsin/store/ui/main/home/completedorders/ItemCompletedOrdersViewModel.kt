package com.brandsin.store.ui.main.home.completedorders

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.main.homepage.StoreOrderItem

class ItemCompletedOrdersViewModel(var item: StoreOrderItem) : BaseViewModel()
{

    fun onItemClicked() {
        setValue(item)
    }

}