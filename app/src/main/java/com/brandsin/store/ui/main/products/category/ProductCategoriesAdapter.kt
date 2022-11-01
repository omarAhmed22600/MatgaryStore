package com.brandsin.store.ui.main.products.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawMyProductsCategoryBinding
import com.brandsin.store.model.main.products.list.ProductCategoriesData
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.SingleLiveEvent
import java.util.*

class ProductCategoriesAdapter : RecyclerView.Adapter<ProductCategoriesAdapter.SubCategoriesHolder>()
{
    var subCategoriesList: ArrayList<ProductCategoriesData> = ArrayList()
    var selectedPosition = 0
    var categoryLiveData = SingleLiveEvent<ProductCategoriesData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoriesHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawMyProductsCategoryBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_my_products_category, parent, false)
        return SubCategoriesHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoriesHolder, position: Int) {
        val itemViewModel = ItemProductCategoryViewModel(subCategoriesList[position])
        holder.binding.viewModel = itemViewModel
        holder.setSelected()
        holder.binding.rawItem.setOnClickListener {
            if (position != selectedPosition)
            {
                categoryLiveData.value = itemViewModel.item
                notifyItemChanged(position)
                if (selectedPosition != -1) {
                    notifyItemChanged(selectedPosition)
                }
                selectedPosition = position
            }
        }
    }

    override fun getItemCount(): Int {
        return subCategoriesList.size
    }

    fun updateList(models: ArrayList<ProductCategoriesData>) {
        subCategoriesList = models
        notifyDataSetChanged()
    }

    inner class SubCategoriesHolder(val binding: RawMyProductsCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setSelected() {
            if (selectedPosition == adapterPosition) {
                binding.rawItem.setBackgroundResource(R.drawable.subcategory_select)
                binding.tvItem.setTextColor(ContextCompat.getColor(MyApp.getInstance(), R.color.color_primary))
            } else {
                binding.rawItem.setBackgroundResource(R.drawable.subcategory_unselect)
                binding.tvItem.setTextColor(ContextCompat.getColor(MyApp.getInstance(), R.color.black))
            }
        }
    }
}
