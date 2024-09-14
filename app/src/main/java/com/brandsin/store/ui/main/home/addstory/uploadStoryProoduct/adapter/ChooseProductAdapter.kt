package com.brandsin.store.ui.main.home.addstory.uploadStoryProoduct.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemChooseProductBinding
import com.brandsin.store.model.main.products.list.ProductsItem
import com.bumptech.glide.Glide

class ChooseProductAdapter(
    private val btnItemClickCallBack: (productsItem: ProductsItem) -> Unit,
) : RecyclerView.Adapter<ChooseProductAdapter.ChooseProductViewHolder>(), Filterable {

    private var productsItemList: MutableList<ProductsItem?>? = ArrayList()
    private var originalItemList: MutableList<ProductsItem?>? = ArrayList()

    private lateinit var binding: ItemChooseProductBinding

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ChooseProductViewHolder(itemView: ItemChooseProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgProduct = itemView.imgProduct
        val productName = itemView.productName
        val productPrice = itemView.productPrice
        val icChecked = itemView.icChecked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseProductViewHolder {
        binding =
            ItemChooseProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChooseProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseProductViewHolder, position: Int) {
        val item = productsItemList!![position]

        // bind view
        bindData(holder, item!!, position)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(
        holder: ChooseProductViewHolder,
        productItem: ProductsItem,
        position: Int
    ) {

        Glide.with(holder.itemView.context)
            .load(productItem.image)
            .error(R.drawable.app_logo)
            .into(holder.imgProduct)

        holder.productName.text = productItem.name ?: ""

        holder.productPrice.text =
            productItem.skus?.get(0)?.sale_price.toString() + " " + holder.itemView.context.getString(
                R.string.currency
            )

        /*if (productsItemList?.get(holder.absoluteAdapterPosition)!!.isSelected) { // isSelected == true
            //productItem.isSelected = false
            productsItemList?.get(holder.absoluteAdapterPosition)!!.isSelected = false
            holder.icChecked.setImageResource(R.drawable.ic_checked)
        } else { // isSelected == true
            productsItemList?.get(holder.absoluteAdapterPosition)!!.isSelected = true
            holder.icChecked.setImageResource(R.drawable.ic_radio_button_unchecked_24px)
        }*/

        holder.icChecked.isChecked = holder.absoluteAdapterPosition == selectedPosition

        holder.itemView.setOnClickListener {
            if (position != selectedPosition) {
                selectedPosition = position
                notifyDataSetChanged() // Notify the adapter that the data set has changed
            }
            btnItemClickCallBack.invoke(productItem)
        }
    }
    // haa

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return productsItemList?.size ?: 0
    }

    fun submitData(productsItems: MutableList<ProductsItem?>?) {
        originalItemList = productsItems
        productsItemList = productsItems
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().lowercase()

                val filteredList = mutableListOf<ProductsItem?>()

                if (query.isEmpty()) {
                    filteredList.addAll(originalItemList.orEmpty())
                } else {
                    for (story in originalItemList.orEmpty()) {
                        if (story?.name?.lowercase()?.contains(query) == true) {
                            filteredList.add(story)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST", "NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                productsItemList = results?.values as ArrayList<ProductsItem?>?
                notifyDataSetChanged()
            }
        }
    }
}