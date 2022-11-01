package com.brandsin.store.ui.dialogs.storetypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawStoreTypeBinding
import com.brandsin.store.model.auth.StoreTypeItem
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.profile.updatestore.UpdateStoreRequest
import com.brandsin.store.utils.SingleLiveEvent

class StoreTypesAdapter : RecyclerView.Adapter<StoreTypesAdapter.OrderAddonsHolder>()
{
    var orderAddonsList: ArrayList<StoreTypeItem> = ArrayList()
    var orderAddonsLiveData = SingleLiveEvent<StoreTypeItem>()
    var storeRequestData = StoreRegister()
    var updateRequestData = UpdateStoreRequest()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAddonsHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawStoreTypeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_store_type, parent, false)
        return OrderAddonsHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAddonsHolder, position: Int)
    {
        val itemViewModel = ItemStoreTypeViewModel(orderAddonsList[position])
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

    fun getItem(pos:Int): StoreTypeItem {
        return orderAddonsList[pos]
    }

    override fun getItemCount(): Int {
        return orderAddonsList.size
    }

    fun updateList(models: ArrayList<StoreTypeItem>, storeRequest: StoreRegister, updateRequest: UpdateStoreRequest) {
        orderAddonsList = models
        storeRequestData= storeRequest
        updateRequestData= updateRequest
    }

    inner class OrderAddonsHolder(val binding: RawStoreTypeBinding) : RecyclerView.ViewHolder(binding.root)
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
                if (storeRequestData.categories !=null) {
                    when {
                        storeRequestData.categories!!.contains(orderAddonsList[adapterPosition].id) -> {
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
                if (updateRequestData.categories !=null) {
                    when {
                        updateRequestData.categories!!.contains(orderAddonsList[adapterPosition].id) -> {
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
