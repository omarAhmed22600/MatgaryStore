package com.brandsin.store.ui.menu.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawHomeAboutBinding
import com.brandsin.store.model.menu.about.AboutItem
import com.brandsin.store.utils.SingleLiveEvent

class AboutAdapter : RecyclerView.Adapter<AboutAdapter.AboutHolder>() {

    private var aboutList: ArrayList<AboutItem> = ArrayList()
    var aboutLiveData = SingleLiveEvent<AboutItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RawHomeAboutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.raw_home_about, parent, false)
        return AboutHolder(binding)
    }

    override fun onBindViewHolder(holder: AboutHolder, position: Int) {
        val itemViewModel = ItemAboutViewModel(aboutList[position])
        holder.binding.viewModel = itemViewModel

        holder.binding.rawLayout.setOnClickListener {
            aboutLiveData.value = itemViewModel.item
        }
    }

    override fun getItemCount(): Int {
        return aboutList.size
    }

    fun updateList(models: ArrayList<AboutItem>) {
        aboutList = models
        notifyDataSetChanged()
    }

    inner class AboutHolder(val binding: RawHomeAboutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
