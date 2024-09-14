package com.brandsin.store.ui.main.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.databinding.ItemCategoryBinding
import com.brandsin.store.ui.main.categories.model.CategoryItem
import com.brandsin.store.utils.PrefMethods

class CategoriesAdapter(
    private val btnDeleteClickCallBack: (category: CategoryItem) -> Unit,
    private val btnEditClickCallBack: (category: CategoryItem) -> Unit,
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var categoriesList: List<CategoryItem?>? = ArrayList()

    private lateinit var binding: ItemCategoryBinding

    inner class CategoriesViewHolder(itemView: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val categoryName = itemView.categoryName
        val delete = itemView.delete
        val edit = itemView.edit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val driver = categoriesList!![position]
        // bind view
        bindData(holder, driver!!)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: CategoriesViewHolder, category: CategoryItem) {
        // Check language
        if (PrefMethods.getLanguage() == "ar") {
            holder.categoryName.text = category.name ?: ""
        } else {
            holder.categoryName.text = category.nameEn ?: ""
        }

        holder.delete.setOnClickListener {
            btnDeleteClickCallBack.invoke(category)
        }

        holder.edit.setOnClickListener {
            btnEditClickCallBack.invoke(category)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList?.size ?: 0
    }

    fun submitData(categories: List<CategoryItem?>?) {
        categoriesList = categories
        notifyDataSetChanged()
    }
}