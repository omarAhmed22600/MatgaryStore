package com.brandsin.store.ui.main.addofffer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawOfferProductsUpdateBinding
import com.brandsin.store.model.main.offers.listoffer.OfferProductItem
import com.brandsin.store.utils.SingleLiveEvent
import com.bumptech.glide.Glide

class UpdateOfferProductsAdapter :
    RecyclerView.Adapter<UpdateOfferProductsAdapter.OfferProductsHolder>() {

    var itemsList: ArrayList<OfferProductItem> = ArrayList()

    var deleteLiveData = SingleLiveEvent<OfferProductItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferProductsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RawOfferProductsUpdateBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_offer_products_update,
            parent,
            false
        )
        return OfferProductsHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferProductsHolder, position: Int) {
        val itemViewModel = UpdateItemOfferProductViewModel(itemsList[position])
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

    fun updateList(modelsUpdate: ArrayList<OfferProductItem>) {
        itemsList = modelsUpdate
        notifyDataSetChanged()
    }

    inner class OfferProductsHolder(val binding: RawOfferProductsUpdateBinding) :
        RecyclerView.ViewHolder(binding.root)

}
