package com.brandsin.store.ui.activity.auth

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseViewModel

class AuthViewModel : BaseViewModel()
{
    val obsShowToolbar = ObservableBoolean()
    val obsTitle = ObservableField<String>()

}