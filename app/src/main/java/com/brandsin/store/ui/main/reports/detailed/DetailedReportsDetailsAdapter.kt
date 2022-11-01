package com.brandsin.store.ui.main.reports.detailed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawDetailedReportDetailsBinding
import com.brandsin.store.model.main.reports.ValueItem
import com.brandsin.store.utils.MyApp
import java.util.*

class DetailedReportsDetailsAdapter : RecyclerView.Adapter<DetailedReportsDetailsAdapter.ReportsDetailsHolder>()
{
    var itemsList: List<ValueItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsDetailsHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawDetailedReportDetailsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_detailed_report_details, parent, false)
        return ReportsDetailsHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportsDetailsHolder, position: Int)
    {
        val itemViewModel = ItemDetailedReportDetailesViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        if (position == 0)
        {
            holder.binding.tvOrdersCount.setTextColor(ContextCompat.getColor(MyApp.getInstance(), R.color.black))
            holder.binding.tvOrdersCost.setTextColor(ContextCompat.getColor(MyApp.getInstance(), R.color.black))
            holder.binding.tvOrdersCount.textSize = 12f
            holder.binding.tvOrdersCost.textSize = 12f
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: List<ValueItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class ReportsDetailsHolder(val binding: RawDetailedReportDetailsBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }
}
