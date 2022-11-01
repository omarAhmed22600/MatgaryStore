package com.brandsin.store.ui.main.home.addstory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.brandsin.store.R
import com.brandsin.store.databinding.RawGalleryBinding
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.SingleLiveEvent
import java.io.File

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {
    var itemsList: ArrayList<String> = ArrayList()
    var uploadLiveData = SingleLiveEvent<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawGalleryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_gallery,
            parent,
            false
        )
        return GalleryHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        val itemViewModel = ItemGalleryViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        if (itemViewModel.item.isEmpty()){
            holder.binding.ivImg.setImageResource(R.drawable.app_background)
        }else {
            Glide.with(MyApp.context).load(itemViewModel.item)
                .error(R.drawable.app_background)
                .into(holder.binding.ivImg)

        }

        holder.binding.ivImg.setOnClickListener {
            uploadLiveData.value  =  File(itemViewModel.item)
        }

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<String>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class GalleryHolder(val binding: RawGalleryBinding) : RecyclerView.ViewHolder(binding.root)

}
