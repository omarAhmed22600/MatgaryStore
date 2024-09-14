package com.brandsin.store.ui.menu.wallet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemWalletBinding
import com.brandsin.store.ui.menu.wallet.model.TransactionsItem
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible

class WalletAdapter : RecyclerView.Adapter<WalletAdapter.ChooseAddressViewHolder>() {

    private var transactionsList: ArrayList<TransactionsItem?>? = ArrayList()

    private lateinit var binding: ItemWalletBinding

    inner class ChooseAddressViewHolder(itemView: ItemWalletBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val icAddOrMinus = itemView.icAddOrMinus
        val balance = itemView.balance
        val note = itemView.note
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAddressViewHolder {
        binding = ItemWalletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChooseAddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseAddressViewHolder, position: Int) {
        val transactionsItem = transactionsList!![position]
        // bind view
        bindData(holder, transactionsItem!!)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: ChooseAddressViewHolder, transactionsItem: TransactionsItem) {

        if (transactionsItem.increase == null || transactionsItem.increase.toDouble() == 0.0) {
            holder.icAddOrMinus.setImageResource(R.drawable.ic_red_minus)
            holder.balance.text =
                transactionsItem.decrease.toString() + " " + holder.itemView.context.getString(R.string.currency)
        } else if (transactionsItem.decrease == null || transactionsItem.decrease.toDouble() == 0.0) {
            holder.icAddOrMinus.setImageResource(R.drawable.ic_green_plus)
            holder.balance.text =
                transactionsItem.increase.toString() + " " + holder.itemView.context.getString(R.string.currency)
        }

        if (transactionsItem.notes?.isNotEmpty() == true || transactionsItem.notes != null) {
            holder.note.visible()
            holder.note.text = transactionsItem.notes.toString()
        } else {
            holder.note.gone()
        }
    }

    override fun getItemCount(): Int {
        return transactionsList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(transactions: ArrayList<TransactionsItem?>?) {
        transactionsList = transactions
        notifyDataSetChanged()
    }
}