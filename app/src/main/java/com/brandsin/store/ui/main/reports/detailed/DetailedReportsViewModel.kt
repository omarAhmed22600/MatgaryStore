package com.brandsin.store.ui.main.reports.detailed

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.reports.DetailsItem
import com.brandsin.store.model.main.reports.ReportsDetailsResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailedReportsViewModel : BaseViewModel()
{
    var reportsAdapter  = DetailedReportsAdapter()
    var reportsList= ArrayList<DetailsItem>()
    var type = "daily" // monthly
    var from = ""
    var to = ""

    fun getReports()
    {
        setShowProgress(true)
        obsIsFull.set(false)
        obsIsEmpty.set(false)
        requestCall<ReportsDetailsResponse?>({ withContext(Dispatchers.IO) {
            return@withContext getApiRepo()
                    .getReportsDetails(PrefMethods.getStoreData()!!.id!!,50,0,type,from, to) }
        })
        { res ->
            when  {
                res!!.data!!.isNotEmpty() -> {
                    obsIsFull.set(true)
                    obsIsEmpty.set(false)
                    setShowProgress(false)

                    reportsList = res.data as ArrayList<DetailsItem>
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

    init {
        getReports()
    }

    fun onCalendarClicked()
    {
        setValue(Codes.SHOW_CALENDAR_DIALOG)
    }

}