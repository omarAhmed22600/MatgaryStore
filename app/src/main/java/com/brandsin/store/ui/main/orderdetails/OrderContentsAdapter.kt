package com.brandsin.store.ui.main.orderdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawOrderContentBinding
import com.brandsin.store.model.main.homepage.ItemsItem
import com.bumptech.glide.Glide

class OrderContentsAdapter : RecyclerView.Adapter<OrderContentsAdapter.OrderContentsHolder>() {

    var itemsList: ArrayList<ItemsItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderContentsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RawOrderContentBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.raw_order_content, parent, false)
        return OrderContentsHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderContentsHolder, position: Int) {
        val itemViewModel = ItemOrderContentViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        Glide.with(holder.itemView.context)
            .load(itemViewModel.item.image)
            .error(R.drawable.app_logo)
            .into(holder.binding.imgProduct)

        if (position == itemsList.size - 1) {
            holder.binding.seperator.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<ItemsItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class OrderContentsHolder(val binding: RawOrderContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
