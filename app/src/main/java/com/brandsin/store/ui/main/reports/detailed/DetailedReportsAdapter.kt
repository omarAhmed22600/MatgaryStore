package com.brandsin.store.ui.main.reports.detailed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawDetailedReportItemBinding
import com.brandsin.store.model.main.reports.DetailsItem
import java.util.*

class DetailedReportsAdapter : RecyclerView.Adapter<DetailedReportsAdapter.OrderReportsHolder>()
{
    var itemsList: List<DetailsItem> = ArrayList()
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReportsHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawDetailedReportItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_detailed_report_item, parent, false)
        return OrderReportsHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderReportsHolder, position: Int)
    {
        val itemViewModel = ItemDetailedReportViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        holder.setSelected()
        holder.binding.tvTitle.setOnClickListener {
            notifyItemChanged(position)
            if (selectedPosition != -1) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = position
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: List<DetailsItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class OrderReportsHolder(val binding: RawDetailedReportItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun setSelected() {
            if (selectedPosition == adapterPosition) {
                binding.rvItems.visibility = View.VISIBLE
            } else {
                binding.rvItems.visibility = View.GONE
            }
        }
    }
}
