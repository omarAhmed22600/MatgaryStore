package com.brandsin.store.ui.main.reports.total

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.reports.DataItem
import com.brandsin.store.model.main.reports.ReportsTotalResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TotalReportsViewModel : BaseViewModel() {

    var reportsAdapter = ReportsAdapter()
    private var reportsList = ArrayList<DataItem>()

    var type = "daily" // monthly
    var from = ""
    var to = ""

    fun getReports() {
        setShowProgress(true)
        obsIsFull.set(false)
        obsIsEmpty.set(false)

        requestCall<ReportsTotalResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                    .getReportsTotal(PrefMethods.getStoreData()!!.id!!, 50, 0, type, from, to)
            }
        })
        { res ->
            when {
                res!!.data!!.isNotEmpty() -> {
                    obsIsFull.set(true)
                    obsIsEmpty.set(false)
                    setShowProgress(false)

                    reportsList = res.data as ArrayList<DataItem>
                    reportsAdapter.updateList(reportsList)
                    reportsAdapter.notifyDataSetChanged()
                }

                else -> {
                    obsIsFull.set(false)
                    obsIsEmpty.set(true)
                    setShowProgress(false)
                }
            }
        }
    }

    fun onCalendarClicked() {
        setValue(Codes.SHOW_CALENDAR_DIALOG)
    }
}