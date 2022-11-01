package com.brandsin.store.ui.main.home.completedorders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawCompletedOrderBinding
import com.brandsin.store.model.main.homepage.StoreOrderItem
import com.brandsin.store.utils.SingleLiveEvent

import java.util.*

class CompletedOrdersAdapter : RecyclerView.Adapter<CompletedOrdersAdapter.CompletedOrdersHolder>()
{
    var itemsList: ArrayList<StoreOrderItem> = ArrayList()
    var completedOrderItemLiveData = SingleLiveEvent<StoreOrderItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedOrdersHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawCompletedOrderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_completed_order, parent, false)
        return CompletedOrdersHolder(binding)
    }

    override fun onBindViewHolder(holder: CompletedOrdersHolder, position: Int)
    {
        val itemViewModel = ItemCompletedOrdersViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel
        itemViewModel.mutableLiveData.observeForever {
            completedOrderItemLiveData.value = itemViewModel.item
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<StoreOrderItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class CompletedOrdersHolder(val binding: RawCompletedOrderBinding) : RecyclerView.ViewHolder(binding.root)
}
