package com.brandsin.store.ui.main.orderdetails

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.main.homepage.ItemsItem

class ItemOrderContentViewModel(var item: ItemsItem) : BaseViewModel()
{
    var contents = ObservableField<String>()
    private val contentsList = ArrayList<String>()

    init
    {
        when {
            item.addons?.isNotEmpty() == true && item.addons?.size != 0 -> {
                item.addons?.forEach { item ->
                    contentsList.add(item?.name.toString())
                }

                contents.set(contentsList.joinToString { it -> it })

                obsSize.set(1)
            }
            else -> {
                obsSize.set(0)
            }
        }
    }

}