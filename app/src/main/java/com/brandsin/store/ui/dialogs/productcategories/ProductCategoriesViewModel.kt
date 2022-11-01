package com.brandsin.store.ui.dialogs.productcategories

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesData

class ProductCategoriesViewModel : BaseViewModel()
{
    var productCategoriesAdapter  = ProductCategoriesAdapter()
    var categoriesList: ArrayList<ProductCategoriesData> = ArrayList()

    fun onSaveClicked() {
        setValue(Codes.CONFIRM_CLICKED)
    }

    fun onCancelClicked() {
        setValue(Codes.CANCEL_CLICKED)
    }
}