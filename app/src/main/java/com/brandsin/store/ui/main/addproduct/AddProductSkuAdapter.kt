package com.brandsin.store.ui.main.addproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawAddProductSkuBinding
import com.brandsin.store.model.main.products.add.SkuAddProductItem
import com.brandsin.store.utils.SingleLiveEvent
import java.util.*

class AddProductSkuAdapter  : RecyclerView.Adapter<AddProductSkuAdapter.ProductSkuHolder>() {

    var productSkuLiveData = SingleLiveEvent<ArrayList<SkuAddProductItem>>()
    var size=1
    var sku = ArrayList<SkuAddProductItem>()
    var skuItem = SkuAddProductItem()
    var chcek = false

    var productUnitLiveData = SingleLiveEvent<Int>()
    var unitId = ""
    var unitName = ""
    var positionSku = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSkuHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawAddProductSkuBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_add_product_sku,
            parent,
            false
        )
        return ProductSkuHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductSkuHolder, position: Int) {

//        val itemViewModel = ItemProductSkuViewModel(itemsList[position])
//        holder.binding.viewModel = itemViewModel

        if (unitName.isNotEmpty() && positionSku == position){
            holder.binding.productUnit.text = unitName
        }

        if (chcek) {
            skuItem = SkuAddProductItem()
//            sku = ArrayList<SkuAddProductItem>()
            skuItem.id = position
            skuItem.name = holder.binding.size.text.toString()
            skuItem.inventory_value = holder.binding.inventoryValue.text.toString()
            skuItem.regular_price = holder.binding.price.text.toString()
            skuItem.sale_price = holder.binding.salePrice.text.toString()
            skuItem.code = holder.binding.productCode.text.toString()
            if (unitId.isNotEmpty()){
                skuItem.unitId= unitId
            }
            if (sku.isNullOrEmpty()){
                sku.add(position, skuItem)
            }else{
                if (sku[sku.size - 1].id != (position-1) && position == sku[position].id){
                    sku[position] = skuItem
                }else{
                    sku.add(position, skuItem)
                }
            }
            productSkuLiveData.value = sku
        }

        holder.binding.productUnit.setOnClickListener {
            productUnitLiveData.value = position
        }
    }

    override fun getItemCount(): Int {
        return size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateList(productUnitId: String, productUnitName: String, skuPosition: Int) {
        if (productUnitId.isEmpty()) {
            size += 1
            chcek = false
            notifyDataSetChanged()
        }else{
            positionSku = skuPosition
            unitId = productUnitId
            unitName = productUnitName
            chcek = false
            notifyDataSetChanged()
        }
    }

    fun getData() {
        chcek = true
        notifyDataSetChanged()
    }

    inner class ProductSkuHolder(val binding: RawAddProductSkuBinding) : RecyclerView.ViewHolder(
        binding.root
    )

}