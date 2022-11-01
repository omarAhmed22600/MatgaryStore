package com.brandsin.store.ui.dialogs.storetags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawStoreTagBinding
import com.brandsin.store.model.auth.StoreTagsItem
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.profile.updatestore.UpdateStoreRequest

import com.brandsin.store.utils.SingleLiveEvent

class StoreTagsAdapter : RecyclerView.Adapter<StoreTagsAdapter.OrderAddonsHolder>()
{
    var orderAddonsList: ArrayList<StoreTagsItem> = ArrayList()
    var orderAddonsLiveData = SingleLiveEvent<StoreTagsItem>()
    var storeRequestData = StoreRegister()
    var updateRequestData = UpdateStoreRequest()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAddonsHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawStoreTagBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_store_tag, parent, false)
        return OrderAddonsHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAddonsHolder, position: Int)
    {
        val itemViewModel = ItemStoreTagViewModel(orderAddonsList[position])
        holder.binding.viewModel = itemViewModel

        holder.setSelected(0)
        holder.binding.rawLayout.setOnClickListener {
            when {
                itemViewModel.item.isSelected -> {
                    itemViewModel.item.isSelected = false
                    holder.setSelected(1)
                }
                else -> {
                    itemViewModel.item.isSelected = true
                    holder.setSelected(1)

                }
            }
            orderAddonsLiveData.value = itemViewModel.item
        }
    }

    fun getItem(pos:Int): StoreTagsItem {
        return orderAddonsList[pos]
    }

    override fun getItemCount(): Int {
        return orderAddonsList.size
    }

    fun updateList(models: ArrayList<StoreTagsItem>, storeRequest: StoreRegister, updateRequest: UpdateStoreRequest) {
        orderAddonsList = models
        storeRequestData= storeRequest
        updateRequestData= updateRequest
    }

    inner class OrderAddonsHolder(val binding: RawStoreTagBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun setSelected(i: Int)
        {
            if (i==1) {
                when {
                    orderAddonsList[adapterPosition].isSelected -> {
                        binding.ivSelected.setImageResource(R.drawable.ic_size_check_box_24px)
                    }
                    else -> {
                        binding.ivSelected.setImageResource(R.drawable.ic_check_box_outline_blank_24px)
                    }
                }
            }else if (i==0){
                if (storeRequestData.tags !=null) {
                    when {
                        storeRequestData.tags!!.contains(orderAddonsList[adapterPosition].id) -> {
                            binding.ivSelected.setImageResource(R.drawable.ic_size_check_box_24px)
                            orderAddonsList[adapterPosition].isSelected = true
                            orderAddonsLiveData.value = orderAddonsList[adapterPosition]
                        }
                        else -> {
                            binding.ivSelected.setImageResource(R.drawable.ic_check_box_outline_blank_24px)
                            orderAddonsList[adapterPosition].isSelected = false
                        }
                    }
                }
                if (updateRequestData.tags !=null) {
                    when {
                        updateRequestData.tags!!.contains(orderAddonsList[adapterPosition].id) -> {
                            binding.ivSelected.setImageResource(R.drawable.ic_size_check_box_24px)
                            orderAddonsList[adapterPosition].isSelected = true
                            orderAddonsLiveData.value = orderAddonsList[adapterPosition]
                        }
                        else -> {
                            binding.ivSelected.setImageResource(R.drawable.ic_check_box_outline_blank_24px)
                            orderAddonsList[adapterPosition].isSelected = false
                        }
                    }
                }
            }
        }
    }
}