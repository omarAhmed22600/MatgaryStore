package com.brandsin.store.ui.main.subscriptions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemSubscriptionPlanBinding
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible

class SubscriptionsAdapter(
    private val btnItemClickCallBack: (subscriptionPlansItem: List<SubscriptionPlansItem?>?, position: Int) -> Unit,
) : RecyclerView.Adapter<SubscriptionsAdapter.SubscriptionsViewHolder>() {

    private var subscriptionPlansList: List<SubscriptionPlansItem?>? = ArrayList()

    private lateinit var binding: ItemSubscriptionPlanBinding

    inner class SubscriptionsViewHolder(itemView: ItemSubscriptionPlanBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val constraintLayout = itemView.constraintLayout
        val planName = itemView.planName
        val planPrice = itemView.planPrice
        val planDesc = itemView.planDesc
        val imgSelected = itemView.imgSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionsViewHolder {
        binding =
            ItemSubscriptionPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionsViewHolder, position: Int) {
        val subscriptionPlanItem = subscriptionPlansList!![position]
        // bind view
        bindData(holder, subscriptionPlanItem!!)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    private fun bindData(
        holder: SubscriptionsViewHolder,
        subscriptionPlansItem: SubscriptionPlansItem
    ) {
        // Check language
        holder.planName.text = subscriptionPlansItem.name.toString()
        holder.planPrice.text =
            subscriptionPlansItem.price.toString() + " " + holder.itemView.context.getString(R.string.currency)
        holder.planDesc.text = subscriptionPlansItem.description.toString()

        if (subscriptionPlansItem.isSelected == true) { // isSelected == true
            holder.imgSelected.visible()
            holder.constraintLayout.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.bg_rectangle_green_stroke
            )
            subscriptionPlansItem.isSelected = false
        } else { // isSelected == true
            holder.imgSelected.gone()
            holder.constraintLayout.background =
                ContextCompat.getDrawable(holder.itemView.context, R.color.white)
            subscriptionPlansItem.isSelected = true
        }

        holder.itemView.setOnClickListener {
            btnItemClickCallBack.invoke(subscriptionPlansList, holder.absoluteAdapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return subscriptionPlansList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(subscriptionsList: List<SubscriptionPlansItem?>?) {
        subscriptionPlansList = subscriptionsList
        notifyDataSetChanged()
    }
}