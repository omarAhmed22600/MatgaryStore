package com.brandsin.store.ui.dialogs.rejectOrder.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.databinding.ItemRejectedReasonBinding
import com.brandsin.store.ui.dialogs.rejectOrder.model.RejectReasonModel

class RejectedReasonAdapter(
    // private val btnItemClickCallBack: (rejectReason: RejectReasonModel) -> Unit,
) : RecyclerView.Adapter<RejectedReasonAdapter.RefundableProductViewHolder>() {

    private var rejectReasonsList: List<RejectReasonModel?>? = ArrayList()

    private lateinit var binding: ItemRejectedReasonBinding

    private var lastCheckedRB: RadioButton? = null

    inner class RefundableProductViewHolder(itemView: ItemRejectedReasonBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val radioBtnReason = itemView.radioBtnReason
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefundableProductViewHolder {
        binding =
            ItemRejectedReasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RefundableProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RefundableProductViewHolder, position: Int) {
        val item = rejectReasonsList!![position]

        // bind view
        bindData(holder, item!!)
    }

    private fun bindData(
        holder: RefundableProductViewHolder,
        rejectReason: RejectReasonModel
    ) {
        holder.radioBtnReason.text = rejectReason.reason ?: ""

        holder.radioBtnReason.setOnClickListener {
            if (lastCheckedRB != null) {
                lastCheckedRB?.isChecked = false
            }
            lastCheckedRB =
                if (lastCheckedRB == holder.radioBtnReason) null else holder.radioBtnReason
        }

        /*holder.itemView.setOnClickListener {
            btnItemClickCallBack.invoke(rejectReasonsList, holder.absoluteAdapterPosition)
        }*/
    }

    override fun getItemCount(): Int {
        return rejectReasonsList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(rejectReasons: List<RejectReasonModel?>?) {
        rejectReasonsList = rejectReasons
        notifyDataSetChanged()
    }
}