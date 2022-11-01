package com.brandsin.store.ui.dialogs.offertime

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.offertime.date.OrderDatesAdapter

class DateViewModel : BaseViewModel()
{
    var datesAdapter  = OrderDatesAdapter()
    var selectedDate = ""
    var selectedTime = ""

    fun setDates(itemsList: ArrayList<OfferDateItem>) {
        datesAdapter.updateList(itemsList,selectedDate)
        datesAdapter.notifyDataSetChanged()

        if (selectedTime.isEmpty()) {
            selectedTime = itemsList[0].date.toString()
        }
        obsSize.set(10)
    }

    fun onConfirmClicked()
    {
        setValue(Codes.CONFIRM_CLICKED)
    }
}