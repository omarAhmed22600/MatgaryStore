package com.brandsin.store.ui.main.updateproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawUpdateProductSkuBinding
import com.brandsin.store.model.main.products.DataItem
import com.brandsin.store.model.main.products.update.SkuUpdateProductItem
import com.brandsin.store.utils.SingleLiveEvent
import java.util.ArrayList

class UpdateProductSkuAdapter : RecyclerView.Adapter<UpdateProductSkuAdapter.ProductSkuHolder>() {

    var productSkuLiveData = SingleLiveEvent<ArrayList<SkuUpdateProductItem>>()
    var size=1
    var sku = ArrayList<SkuUpdateProductItem>()
    var skuItem = SkuUpdateProductItem()
    var chcek = false
    var setSkuData = true

    var productUnitLiveData = SingleLiveEvent<Int>()
    var unitId = ""
    var unitName = ""
    var positionSku = -1

    var unitList: ArrayList<DataItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSkuHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawUpdateProductSkuBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_update_product_sku, parent, false)
        return ProductSkuHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductSkuHolder, position: Int) {

//        val itemViewModel = ItemProductSkuViewModel(itemsList[position])
//        holder.binding.viewModel = itemViewModel

        if (setSkuData ){
//            &&sku.size <= size && sku.size-1 >= position ) {
            var item = sku[position]
            holder.binding.size.setText(item.name)
            holder.binding.inventoryValue.setText(item.inventory_value)
            holder.binding.price.setText(item.regular_price)
            holder.binding.salePrice.setText(item.sale_price)
            holder.binding.productCode.setText(item.code)
            for (x in unitList) {
                if (item.unitId == x.id.toString()) {
                    unitId = x.id.toString()
                    unitName = x.name.toString()
                    holder.binding.productUnit.text = unitName
                }
            }
        }

        if (unitName.isNotEmpty() && positionSku == position){
            holder.binding.productUnit.text = unitName
        }

        if (chcek) {
            skuItem = SkuUpdateProductItem()
//            sku = ArrayList<SkuUpdateProductItem>()
            if (sku.lastIndex < position) {
                skuItem.id = position
            }else{
                skuItem.id = sku[position].id
            }
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
                if (sku.lastIndex < position) {
                    sku.add(position, skuItem)
                }else{
                    sku[position] = skuItem
                }

//                if (sku[sku.size-1].id != (position-1) && position == sku[position].id){
//                    sku[position] = skuItem
//                }else{
//                    sku.add(position, skuItem)
//                }
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

    fun updateList(productUnitId: String, productUnitName: String, skuPosition: Int) {
        if (productUnitId.isEmpty()) {
            size += 1
            chcek = false
            setSkuData = false
            notifyDataSetChanged()
        }else{
            positionSku = skuPosition
            unitId = productUnitId
            unitName = productUnitName
            chcek = false
            setSkuData = false
            notifyDataSetChanged()
        }
    }

    fun getData() {
        chcek = true
        notifyDataSetChanged()
    }
    fun setData(models: ArrayList<SkuUpdateProductItem>, getUnitList: ArrayList<DataItem>) {
        sku = models
        size = sku.size
        unitList = getUnitList
        chcek = false
        notifyDataSetChanged()
    }

    inner class ProductSkuHolder(val binding: RawUpdateProductSkuBinding) : RecyclerView.ViewHolder(binding.root)

}
