package com.brandsin.store.ui.main.discountCoupon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemDiscountCouponBinding
import com.brandsin.store.ui.main.discountCoupon.model.CouponItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiscountCouponsAdapter(
    private val btnEditClickCallBack: (couponItem: CouponItem) -> Unit,
    private val btnDeleteClickCallBack: (couponItem: CouponItem) -> Unit,
) : RecyclerView.Adapter<DiscountCouponsAdapter.DiscountCouponsViewHolder>() {

    private var couponItemsList: List<CouponItem?>? = ArrayList()

    private lateinit var binding: ItemDiscountCouponBinding

    inner class DiscountCouponsViewHolder(itemView: ItemDiscountCouponBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val couponName = itemView.couponName
        val couponPrice = itemView.couponPrice
        val couponDate = itemView.couponDate
        val edit = itemView.edit
        val delete = itemView.delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountCouponsViewHolder {
        binding =
            ItemDiscountCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscountCouponsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscountCouponsViewHolder, position: Int) {
        val couponItemsList = couponItemsList!![position]
        // bind view
        bindData(holder, couponItemsList!!)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(
        holder: DiscountCouponsViewHolder,
        couponItem: CouponItem
    ) {
        // Check language
        holder.couponName.text = couponItem.code.toString()

        if (couponItem.type == "fixed"){
            holder.couponPrice.text =
                couponItem.value.toString() + " " + holder.itemView.context.getString(R.string.currency)
        }else {
            holder.couponPrice.text =
                couponItem.value.toString() + " %"
        }

        holder.couponDate.text =
            "${holder.itemView.context.getString(R.string.from)} ${formatDate(couponItem.start.toString())} ${
                holder.itemView.context.getString(R.string.to)
            } ${formatDate(couponItem.expiry.toString())}"

        holder.edit.setOnClickListener {
            btnEditClickCallBack.invoke(couponItem)
        }

        holder.delete.setOnClickListener {
            btnDeleteClickCallBack.invoke(couponItem)
        }
    }

    override fun getItemCount(): Int {
        return couponItemsList?.size ?: 0
    }

    private fun formatDate(data: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        // Parse the input date string
        // val parsedDate = inputFormat.parse(data)

        // Format the parsed date to the desired output format
        return  outputFormat.format(inputFormat.parse(data) as Date)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(couponsList: List<CouponItem?>?) {
        couponItemsList = couponsList
        notifyDataSetChanged()
    }
}