package com.brandsin.store.ui.main.reports.detailed

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawDetailedReportItemBinding
import com.brandsin.store.model.main.reports.DetailsItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailedReportsAdapter : RecyclerView.Adapter<DetailedReportsAdapter.OrderReportsHolder>() {

    var itemsList: List<DetailsItem> = ArrayList()

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReportsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RawDetailedReportItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_detailed_report_item,
            parent,
            false
        )
        return OrderReportsHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OrderReportsHolder, position: Int) {
        val itemViewModel = ItemDetailedReportViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        /*val parsedDate = DateTimeFormatter
            .ofPattern("MMMM, yyyy", Locale.ENGLISH)
            .parse(itemViewModel.item.date)

        // Format the parsed date to get the pattern
        val formattedDate = DateTimeFormatter.ofPattern("MMMM, yyyy", Locale.ENGLISH)
            .format(parsedDate)

        // Check if the formatted date matches the input string
        // return formattedDate == dateString

        if (formattedDate == itemViewModel.item.date) {
            println("MMMM, yyyy")
            holder.binding.tvTitle.text =
                SimpleDateFormat("MMMM, yyyy", Locale("ar")).format(itemViewModel.item.date.toString())
        }*/

        holder.setSelected() // November, 2023

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

    private fun formatDate(date: Date, locale: Locale): String {
        // Create a SimpleDateFormat with the desired locale
        val sdf = SimpleDateFormat("dd MMMM yyyy", locale)

        // Format the date
        return sdf.format(date)
    }

    inner class OrderReportsHolder(val binding: RawDetailedReportItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setSelected() {
            if (selectedPosition == adapterPosition) {
                binding.rvItems.visibility = View.VISIBLE
            } else {
                binding.rvItems.visibility = View.GONE
            }
        }
    }
}
