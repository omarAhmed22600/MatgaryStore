package com.brandsin.store.ui.main.refundableProduct.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemRefundableProductBinding
import com.brandsin.store.ui.main.refundableProduct.model.RefundableProductItem
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RefundableProductAdapter(
    private val btnDetailsClickCallBack: (offerAndFeature: RefundableProductItem) -> Unit,
) : RecyclerView.Adapter<RefundableProductAdapter.RefundableProductViewHolder>() {

    private var refundableProductsList: List<RefundableProductItem?>? = ArrayList()

    private lateinit var binding: ItemRefundableProductBinding

    inner class RefundableProductViewHolder(itemView: ItemRefundableProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgProduct = itemView.imgProduct
        val productName = itemView.productName
        val orderNumber = itemView.orderNumber
        val orderDate = itemView.orderDate
        val statusOrder = itemView.statusOrder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefundableProductViewHolder {
        binding =
            ItemRefundableProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RefundableProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RefundableProductViewHolder, position: Int) {
        val item = refundableProductsList!![position]

        // bind view
        bindData(holder, item!!)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(
        holder: RefundableProductViewHolder,
        refundableProduct: RefundableProductItem
    ) {

        holder.productName.text = refundableProduct.name ?: ""

        holder.orderNumber.text =
            holder.itemView.context.getString(R.string.order_number) + ": " + refundableProduct.orderNumber.toString()

        holder.orderDate.text = formatDate(refundableProduct.refundableDate ?: "")

        Glide.with(holder.itemView.context)
            .load(refundableProduct.image)
            .into(holder.imgProduct)

        checkRefundableStatus(holder, refundableProduct)

        holder.itemView.rootView.setOnClickListener {
            btnDetailsClickCallBack.invoke(refundableProduct)
        }
    }

    private fun checkRefundableStatus(
        holder: RefundableProductViewHolder,
        refundableProduct: RefundableProductItem
    ) {
        when {
            refundableProduct.refundableStatus?.contains("pending") == true ||
                    refundableProduct.refundableStatus?.contains("waiting") == true -> {
                holder.statusOrder.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context, R.color.color_primary
                    )
                )
                holder.statusOrder.text = holder.itemView.context.getString(R.string.pending)
                holder.statusOrder.isClickable = false
            }

            refundableProduct.refundableStatus?.contains("rejected") == true -> {
                holder.statusOrder.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.order_rejected_color)
                )
                holder.statusOrder.text = holder.itemView.context.getString(R.string.reject)
                holder.statusOrder.isClickable = false
            }

            refundableProduct.refundableStatus?.contains("approval") == true -> {
                holder.statusOrder.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context, R.color.completed
                    )
                )
                holder.statusOrder.text = holder.itemView.context.getString(R.string.completed)
                holder.statusOrder.isClickable = false
            }

            else -> {
                holder.statusOrder.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context, R.color.color_primary
                    )
                )
                holder.statusOrder.text = holder.itemView.context.getString(R.string.return_product)
                holder.statusOrder.isClickable = true
            }
        }
    }

    override fun getItemCount(): Int {
        return refundableProductsList?.size ?: 0
    }

    private fun formatDate(data: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy  HH:mm", Locale.getDefault())

        // Format the parsed date to the desired output format
        return  outputFormat.format(inputFormat.parse(data) as Date)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(refundableProducts: List<RefundableProductItem?>?) {
        refundableProductsList = refundableProducts
        notifyDataSetChanged()
    }
}