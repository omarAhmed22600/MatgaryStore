package com.brandsin.store.ui.menu.help.helpques

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawHelpQuesBinding
import com.brandsin.store.utils.SingleLiveEvent
import com.brandsin.user.model.menu.help.HelpQuesItem
import java.util.*

class HelpAdapter : RecyclerView.Adapter<HelpAdapter.AboutHolder>()
{
    var helpList: ArrayList<HelpQuesItem> = ArrayList()
    var helpLiveData = SingleLiveEvent<HelpQuesItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutHolder
    {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawHelpQuesBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_help_ques, parent, false)
        return AboutHolder(binding)
    }

    override fun onBindViewHolder(holder: AboutHolder, position: Int) {
        val itemViewModel = ItemHelpViewModel(helpList[position])
        holder.binding.viewModel = itemViewModel

        holder.binding.rawLayout.setOnClickListener {
            helpLiveData.value = itemViewModel.item
        }
    }

    override fun getItemCount(): Int {
        return helpList.size
    }

    fun updateList(models: ArrayList<HelpQuesItem>) {
        helpList = models
        notifyDataSetChanged()
    }

    inner class AboutHolder(val binding: RawHelpQuesBinding) : RecyclerView.ViewHolder(binding.root)
}
