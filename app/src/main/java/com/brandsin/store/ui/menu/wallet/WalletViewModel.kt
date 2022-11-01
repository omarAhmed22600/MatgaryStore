package com.brandsin.store.ui.menu.wallet

import com.brandsin.store.database.BaseViewModel

class WalletViewModel : BaseViewModel()
{
    init {
        obsIsLogin.set(true)
    }
}