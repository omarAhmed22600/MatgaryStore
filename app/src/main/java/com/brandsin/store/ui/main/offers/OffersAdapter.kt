package com.brandsin.store.ui.main.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawHomeOffersBinding
import com.brandsin.store.model.main.offers.listoffer.OffersItemDetails
import com.brandsin.store.utils.SingleLiveEvent

class OffersAdapter : RecyclerView.Adapter<OffersAdapter.OffersHolder>()
{
    var offersList: ArrayList<OffersItemDetails> = ArrayList()
    var itemLiveData = SingleLiveEvent<OffersItemDetails>()
    var editLiveData = SingleLiveEvent<OffersItemDetails>()
    var deleteLiveData = SingleLiveEvent<OffersItemDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawHomeOffersBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_home_offers, parent, false)
        return OffersHolder(binding)
    }

    override fun onBindViewHolder(holder: OffersHolder, position: Int) {
        val itemViewModel = ItemOffersViewModel(offersList[position])
        holder.binding.viewModel = itemViewModel

//        holder.binding.rawLayout.setOnClickListener {
//            itemLiveData.value = itemViewModel.item
//        }

        holder.binding.tvDelete.setOnClickListener {
            deleteLiveData.value = itemViewModel.item
        }

        holder.binding.tvEdit.setOnClickListener {
            editLiveData.value = itemViewModel.item
        }

    }

    override fun getItemCount(): Int {
        return offersList.size
    }

    fun updateList(models: ArrayList<OffersItemDetails>?) {
        offersList = models!!
        notifyDataSetChanged()
    }

    inner class OffersHolder(val binding: RawHomeOffersBinding) : RecyclerView.ViewHolder(binding.root)
}
