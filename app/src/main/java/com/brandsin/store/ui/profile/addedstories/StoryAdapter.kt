package com.brandsin.store.ui.profile.addedstories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.brandsin.store.R
import com.brandsin.store.databinding.RawStoryBinding
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.SingleLiveEvent

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryHolder>() {
    var itemsList: ArrayList<StoriesItem> = ArrayList()
    var deleteLiveData = SingleLiveEvent<StoriesItem>()
    var showLiveData = SingleLiveEvent<StoriesItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawStoryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_story,
            parent,
            false
        )
        return StoryHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val itemViewModel = ItemStoryViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        if (itemViewModel.item.media!!.isEmpty()){
            holder.binding.cvTxt.visibility = View.VISIBLE
            holder.binding.cvImg.visibility = View.GONE
            holder.binding.cvVideo.visibility = View.GONE

            holder.binding.tvTxt.text = itemViewModel.item.text.toString()
            holder.binding.tvViewsTxt.text = itemViewModel.item.views.toString()

        }else if (itemViewModel.item.media!![0]!!.mimeType!!.contains("image")){
            holder.binding.cvTxt.visibility = View.GONE
            holder.binding.cvImg.visibility = View.VISIBLE
            holder.binding.cvVideo.visibility = View.GONE

            if (itemViewModel.item.mediaUrl.isNullOrEmpty()) {
                Glide.with(MyApp.context).load(R.drawable.app_background)
                    .error(R.drawable.app_background)
                    .into(holder.binding.ivImg)
            }else{
                Glide.with(MyApp.context).load(itemViewModel.item.mediaUrl).error(R.drawable.app_background).into(holder.binding.ivImg)
            }
            holder.binding.tvViewsTxt.text = itemViewModel.item.views.toString()

        }else if (itemViewModel.item.media!![0]!!.mimeType!!.contains("video")){
            holder.binding.cvTxt.visibility = View.GONE
            holder.binding.cvImg.visibility = View.GONE
            holder.binding.cvVideo.visibility = View.VISIBLE

            if (itemViewModel.item.mediaUrl.isNullOrEmpty()) {
                holder.binding.ivVideo.setImageResource(R.drawable.app_background)
            }else{
                Glide.with(MyApp.context).load("")
                    .thumbnail(Glide.with(MyApp.context).load(itemViewModel.item.mediaUrl))
                    .error(R.drawable.app_background)
                    .into(holder.binding.ivVideo)
            }
            holder.binding.tvViewsTxt.text = itemViewModel.item.views.toString()

        }

        holder.binding.ivCloseTxt.setOnClickListener {
            deleteLiveData.value = itemViewModel.item
        }
        holder.binding.ivCloseImg.setOnClickListener {
            deleteLiveData.value = itemViewModel.item
        }
        holder.binding.ivCloseVideo.setOnClickListener {
            deleteLiveData.value = itemViewModel.item
        }

        holder.binding.consTxt.setOnClickListener {
            showLiveData.value = itemViewModel.item
        }
        holder.binding.consImg.setOnClickListener {
            showLiveData.value = itemViewModel.item
        }
        holder.binding.consVideo.setOnClickListener {
            showLiveData.value = itemViewModel.item
        }

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<StoriesItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class StoryHolder(val binding: RawStoryBinding) : RecyclerView.ViewHolder(binding.root)

}
