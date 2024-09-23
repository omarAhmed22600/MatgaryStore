package com.brandsin.store.ui.main.products.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawMyProductsItemsBinding
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.utils.SingleLiveEvent
import java.util.*

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ProductsHolder>()
{
    var productsList: ArrayList<ProductsItem> = ArrayList()
    var itemLiveData = SingleLiveEvent<ProductsItem>()
    var editLiveData = SingleLiveEvent<ProductsItem>()
    var deleteLiveData = SingleLiveEvent<ProductsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawMyProductsItemsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_my_products_items, parent, false)
        return ProductsHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int)
    {
        val itemViewModel = ItemProductViewModel(productsList[position])
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
        holder.binding.tvStatus.setOnClickListener {
            itemViewModel.toggleStatus(holder.binding.tvStatus) {
                // This will run after the toggleStatus function completes
                holder.binding.tvStatus.text = itemViewModel.item.getStatusText(holder.binding.tvStatus)
                holder.binding.tvStatus.setTextColor(holder.binding.tvStatus.context.getColor(itemViewModel.item.getStatusColor()))
            }
        }



//        holder.binding.tvSave.setOnClickListener {
//            holder.binding.tvEdit.visibility = View.VISIBLE
//            holder.binding.tvPrice.visibility = View.VISIBLE
//           // itemViewModel.item.price = holder.binding.tvEdit.text.toString()
//            holder.binding.editPriceLayout.visibility = View.INVISIBLE
//            holder.binding.tvSave.visibility = View.INVISIBLE
//            notifyItemChanged(position)
//        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun updateList(models: ArrayList<ProductsItem>) {
        productsList = models
        notifyDataSetChanged()
    }

    inner class ProductsHolder(val binding: RawMyProductsItemsBinding) : RecyclerView.ViewHolder(binding.root)
}
