package com.brandsin.store.ui.dialogs.productunit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawProductUnitBinding
import com.brandsin.store.model.main.products.DataItem
import com.brandsin.store.utils.SingleLiveEvent

class ProductUnitAdapter : RecyclerView.Adapter<ProductUnitAdapter.OrderAddonsHolder>()
{
    var orderAddonsList: ArrayList<DataItem> = ArrayList()
    var orderAddonsLiveData = SingleLiveEvent<DataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAddonsHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawProductUnitBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_product_unit, parent, false)
        return OrderAddonsHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAddonsHolder, position: Int)
    {
        val itemViewModel = ItemProductUnitViewModel(orderAddonsList[position])
        holder.binding.viewModel = itemViewModel

        holder.binding.rawLayout.setOnClickListener {
            orderAddonsLiveData.value = itemViewModel.item
        }
    }

    fun getItem(pos:Int): DataItem {
        return orderAddonsList[pos]
    }

    override fun getItemCount(): Int {
        return orderAddonsList.size
    }

    fun updateList(models: ArrayList<DataItem>) {
        orderAddonsList = models
    }

    inner class OrderAddonsHolder(val binding: RawProductUnitBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }
}
