package com.brandsin.store.ui.dialogs.storeDeliveryType

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.databinding.ItemDeliveryTypeBinding

class DeliveryTypeAdapter(
    private val btnItemClickCallBack: (type: String) -> Unit,
) : RecyclerView.Adapter<DeliveryTypeAdapter.DeliveryTypeViewHolder>() {

    private var deliveryTypeList: ArrayList<String>? = ArrayList()

    private lateinit var binding: ItemDeliveryTypeBinding

    inner class DeliveryTypeViewHolder(itemView: ItemDeliveryTypeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val type = itemView.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryTypeViewHolder {
        binding =
            ItemDeliveryTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryTypeViewHolder, position: Int) {
        val item = deliveryTypeList!![position]
        // bind view
        bindData(holder, item)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(
        holder: DeliveryTypeViewHolder,
        deliveryType: String
    ) {
        holder.type.text = deliveryType

        holder.itemView.rootView.setOnClickListener {
            btnItemClickCallBack.invoke(deliveryType)
        }
    }

    override fun getItemCount(): Int {
        return deliveryTypeList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(deliveryTypes: ArrayList<String>?) {
        deliveryTypeList = deliveryTypes
        notifyDataSetChanged()
    }
}