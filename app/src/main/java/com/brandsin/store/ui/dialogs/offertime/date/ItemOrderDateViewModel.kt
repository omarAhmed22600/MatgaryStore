package com.brandsin.store.ui.dialogs.offertime.date

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.ui.dialogs.offertime.OfferDateItem

class ItemOrderDateViewModel(var item: OfferDateItem) : BaseViewModel()
{
    var obsDay = ObservableField("")
    var obsDate = ObservableField("")

    init {
       item.day.let {
           obsDay.set(it!!.take(3))
        }

        item.date.let {
            val dateArr: Array<String> = item.date.toString().split("-".toRegex()).toTypedArray()
            obsDate.set(dateArr[2])
        }
    }
}