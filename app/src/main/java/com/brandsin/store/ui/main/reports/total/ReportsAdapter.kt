package com.brandsin.store.ui.main.reports.total

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawTotalReportBinding
import com.brandsin.store.model.main.reports.DataItem

class ReportsAdapter : RecyclerView.Adapter<ReportsAdapter.OrderReportsHolder>() {

    var itemsList = ArrayList<DataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReportsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RawTotalReportBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.raw_total_report, parent, false)
        return OrderReportsHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderReportsHolder, position: Int) {
        val itemViewModel = ItemReportViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<DataItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class OrderReportsHolder(val binding: RawTotalReportBinding) :
        RecyclerView.ViewHolder(binding.root)
}
