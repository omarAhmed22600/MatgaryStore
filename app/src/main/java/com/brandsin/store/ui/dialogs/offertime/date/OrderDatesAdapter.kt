package com.brandsin.store.ui.dialogs.offertime.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawOrderDateBinding
import com.brandsin.store.ui.dialogs.offertime.OfferDateItem
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.SingleLiveEvent

class OrderDatesAdapter : RecyclerView.Adapter<OrderDatesAdapter.OrderDateHolder>()
{
    var datesList: List<OfferDateItem> = ArrayList()
    var dateLiveData = SingleLiveEvent<OfferDateItem>()
    var selectedPosition = -1

    var selectedDate = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDateHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawOrderDateBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_order_date, parent, false)
        return OrderDateHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDateHolder, position: Int) {
        val itemViewModel = ItemOrderDateViewModel(datesList[position])
        holder.binding.viewModel = itemViewModel

        if (selectedDate.isEmpty()) {
            selectedPosition = 0
            if (selectedPosition== position) {
                dateLiveData.value = itemViewModel.item
            }
        }else{
            if (itemViewModel.item.date == selectedDate){
                selectedPosition = position
                dateLiveData.value = itemViewModel.item
            }
        }
        holder.setSelected()

        holder.binding.rawItem.setOnClickListener {
            selectedDate = itemViewModel.item.date.toString()
            selectedPosition = position
            notifyDataSetChanged()
            dateLiveData.value = itemViewModel.item
        }
    }

    override fun getItemCount(): Int {
        return datesList.size
    }

    fun updateList(models: ArrayList<OfferDateItem>, date: String) {
        datesList = models
        selectedDate = date
        notifyDataSetChanged()
    }

    inner class OrderDateHolder(val binding: RawOrderDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setSelected() {
            when (selectedPosition) {
                adapterPosition -> {
                    binding.tvDate.setTextColor(ContextCompat.getColor(MyApp.getInstance(), R.color.white))
                    binding.tvDate.background = (ContextCompat.getDrawable(MyApp.getInstance(), R.drawable.date_bg))
                    binding.rawItem.background = (ContextCompat.getDrawable(MyApp.getInstance(), R.drawable.day_bg))
                }
                else -> {
                    binding.tvDate.setTextColor(ContextCompat.getColor(MyApp.getInstance(), R.color.hint_color))
                    binding.tvDate.background = null
                    binding.rawItem.background = null
                }
            }
        }
    }
}
