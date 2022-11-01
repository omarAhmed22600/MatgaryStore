package com.brandsin.store.ui.menu.rateapp

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes

class RateAppViewModel : BaseViewModel()
{
    val obsRate = ObservableField<Float>()
    val obsMsg = ObservableField<String>()

    init {
        obsRate.set(5f)
    }

    fun onRateClicked()
    {
        setValue(Codes.RATING_SUCCESS)
    }

}