package com.brandsin.store.ui.main.reports.detailed

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.main.reports.DetailsItem
import com.brandsin.store.model.main.reports.ValueItem

class ItemDetailedReportViewModel(var item: DetailsItem) : BaseViewModel() {
    var detailesAdapter = DetailedReportsDetailsAdapter()

    init {
        detailesAdapter.updateList(item.value as List<ValueItem>)
        detailesAdapter.notifyDataSetChanged()
    }
}