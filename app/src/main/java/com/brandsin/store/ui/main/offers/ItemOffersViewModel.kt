package com.brandsin.store.ui.main.offers

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.main.offers.listoffer.OfferProductItem
import com.brandsin.store.model.main.offers.listoffer.OffersItemDetails
import com.brandsin.store.ui.main.offers.offercontent.OfferProductsAdapter
import java.util.*

class ItemOffersViewModel(var item: OffersItemDetails) : BaseViewModel()
{
    var adapter  = OfferProductsAdapter()

    init {
        adapter.updateList(item.offerProductsList as ArrayList<OfferProductItem>?)
        obsIsLogin.set(true)
    }
}