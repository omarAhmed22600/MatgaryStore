package com.brandsin.store.ui.main.addofffer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawOfferProductsBinding
import com.brandsin.store.model.main.offers.add.DataItem
import com.brandsin.store.utils.SingleLiveEvent
import com.bumptech.glide.Glide

class AddOfferProductsAdapter :
    RecyclerView.Adapter<AddOfferProductsAdapter.OfferProductsHolder>() {

    var itemsList: ArrayList<DataItem> = ArrayList()
    var deleteLiveData = SingleLiveEvent<DataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferProductsHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawOfferProductsBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.raw_offer_products, parent, false)
        return OfferProductsHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferProductsHolder, position: Int) {
        val itemViewModel = ItemOfferProductViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        Glide.with(holder.itemView.context)
            .load(itemViewModel.item.image)
            .error(R.drawable.app_logo)
            .into(holder.binding.imgOfferProduct)

        holder.binding.tvDelete.setOnClickListener {
            deleteLiveData.value = itemViewModel.item
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<DataItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class OfferProductsHolder(val binding: RawOfferProductsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
