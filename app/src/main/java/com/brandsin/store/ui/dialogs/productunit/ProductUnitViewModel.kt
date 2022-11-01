package com.brandsin.store.ui.dialogs.productunit

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.main.products.DataItem

class ProductUnitViewModel : BaseViewModel() {

    var productUnitAdapter = ProductUnitAdapter()
    var unitList: ArrayList<DataItem> = ArrayList()

}