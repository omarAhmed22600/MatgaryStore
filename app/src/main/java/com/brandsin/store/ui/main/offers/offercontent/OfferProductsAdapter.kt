package com.brandsin.store.ui.main.offers.offercontent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawOfferContentBinding
import com.brandsin.store.model.main.offers.listoffer.OfferProductItem
import java.util.*

class OfferProductsAdapter : RecyclerView.Adapter<OfferProductsAdapter.OffersContentHolder>()
{
    var offersList: List<OfferProductItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersContentHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawOfferContentBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_offer_content, parent, false)
        return OffersContentHolder(binding)
    }

    override fun onBindViewHolder(holder: OffersContentHolder, position: Int)
    {
        val itemViewModel = ItemOfferContentViewModel(offersList[position])
        holder.binding.viewModel = itemViewModel
    }

    override fun getItemCount(): Int {
        return offersList.size
    }

    fun updateList(models: ArrayList<OfferProductItem>?) {
        offersList = models!!
        notifyDataSetChanged()
    }

    inner class OffersContentHolder(val binding: RawOfferContentBinding) : RecyclerView.ViewHolder(binding.root)
}
