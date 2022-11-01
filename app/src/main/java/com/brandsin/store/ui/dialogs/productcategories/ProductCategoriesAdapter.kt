package com.brandsin.store.ui.dialogs.productcategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawProductCategoriesBinding
import com.brandsin.store.model.main.products.add.AddProductRequest
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesData
import com.brandsin.store.model.main.products.update.UpdateProductRequest
import com.brandsin.store.utils.SingleLiveEvent

class ProductCategoriesAdapter : RecyclerView.Adapter<ProductCategoriesAdapter.OrderAddonsHolder>()
{
    var orderAddonsList: ArrayList<ProductCategoriesData> = ArrayList()
    var orderAddonsLiveData = SingleLiveEvent<ProductCategoriesData>()
    var addRequestData = AddProductRequest()
    var updateRequestData = UpdateProductRequest()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAddonsHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawProductCategoriesBinding= DataBindingUtil.inflate(layoutInflater, R.layout.raw_product_categories, parent, false)
        return OrderAddonsHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAddonsHolder, position: Int)
    {
        val itemViewModel = ItemProductCategoriesViewModel(orderAddonsList[position])
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

    fun getItem(pos:Int): ProductCategoriesData {
        return orderAddonsList[pos]
    }

    override fun getItemCount(): Int {
        return orderAddonsList.size
    }

    fun updateList(models: ArrayList<ProductCategoriesData>, addProductRequest: AddProductRequest, UpdateProductRequest: UpdateProductRequest) {
        orderAddonsList = models
        addRequestData= addProductRequest
        updateRequestData= UpdateProductRequest
    }

    inner class OrderAddonsHolder(val binding: RawProductCategoriesBinding) : RecyclerView.ViewHolder(binding.root)
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
                if (addRequestData.categoriesIds !=null) {
                    when {
                        addRequestData.categoriesIds!!.contains(orderAddonsList[adapterPosition].id) -> {
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
                if (updateRequestData.categoriesIds !=null) {
                    when {
                        updateRequestData.categoriesIds!!.contains(orderAddonsList[adapterPosition].id) -> {
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
