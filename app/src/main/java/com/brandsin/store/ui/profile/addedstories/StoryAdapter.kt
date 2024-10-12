package com.brandsin.store.ui.profile.addedstories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawStoryBinding
import com.brandsin.store.model.Story
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.SingleLiveEvent
import com.bumptech.glide.Glide
import timber.log.Timber

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryHolder>() {

    var itemsList: ArrayList<Story> = ArrayList()

    var deleteLiveData = SingleLiveEvent<Story>()
    var showLiveData = SingleLiveEvent<Story>()
    var allItemsList = SingleLiveEvent<ArrayList<Story>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
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

        holder.binding.tvViewsTxt.text = itemViewModel.item.views.toString()

        if (itemViewModel.item.media_url.isNullOrEmpty() && itemViewModel.item.text?.isNotEmpty() == true) {
            holder.binding.cvTxt.visibility = View.VISIBLE
            holder.binding.cvImg.visibility = View.GONE
            holder.binding.cvVideo.visibility = View.GONE

            holder.binding.txtStory.text = itemViewModel.item.text.toString()

        } else if (itemViewModel.item.media_url?.endsWith(".jpeg") == true ||
            itemViewModel.item.media_url?.endsWith(".jpg") == true ||
            itemViewModel.item.media_url?.endsWith(".png") == true
        ) {
            holder.binding.cvTxt.visibility = View.GONE
            holder.binding.cvImg.visibility = View.VISIBLE
            holder.binding.cvVideo.visibility = View.GONE

            if (itemViewModel.item.media_url.isNullOrEmpty()) {
                Glide.with(MyApp.context).load(R.drawable.app_background)
                    .error(R.drawable.app_background)
                    .into(holder.binding.ivImg)
            } else {
                Glide.with(MyApp.context).load(itemViewModel.item.media_url)
                    .error(R.drawable.app_background).into(holder.binding.ivImg)
            }

        } else if (itemViewModel.item.media_url?.endsWith("mp4") == true) {
            holder.binding.cvTxt.visibility = View.GONE
            holder.binding.cvImg.visibility = View.GONE
            holder.binding.cvVideo.visibility = View.VISIBLE

            if (itemViewModel.item.media_url.isNullOrEmpty()) {
                holder.binding.ivVideo.setImageResource(R.drawable.app_background)
            } else {
                Glide.with(MyApp.context).load("")
                    .thumbnail(Glide.with(MyApp.context).load(itemViewModel.item.media_url))
                    .error(R.drawable.app_background)
                    .into(holder.binding.ivVideo)
            }

        } else if (itemViewModel.item.media_url?.isNotEmpty() == true && itemViewModel.item.text?.isNotEmpty() == true) {
            holder.binding.cvTxt.visibility = View.VISIBLE
            holder.binding.cvImg.visibility = View.VISIBLE
            holder.binding.cvVideo.visibility = View.GONE

            Glide.with(MyApp.context)
                .load(itemViewModel.item.media_url)
                .error(R.drawable.app_background)
                .into(holder.binding.ivImg)

            holder.binding.txtStory.text = itemViewModel.item.text.toString()
        }
        //TODO
//
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
            Timber.e("item from adapter is ${itemViewModel.item}")
        }
        holder.binding.consImg.setOnClickListener {
            showLiveData.value = itemViewModel.item
            Timber.e("item from adapter is ${itemViewModel.item}")

        }
        holder.binding.consVideo.setOnClickListener {
            showLiveData.value = itemViewModel.item
            Timber.e("item from adapter is ${itemViewModel.item}")

        }

//        holder.binding.consTxt.setOnClickListener {
//            //showLiveData.value = itemViewModel.item
//            allItemsList.value = itemsList
//        }
//        holder.binding.consImg.setOnClickListener {
//            //  showLiveData.value = itemViewModel.item
//            allItemsList.value = itemsList
//        }
//        holder.binding.consVideo.setOnClickListener {
//            showLiveData.value = itemViewModel.item
//            allItemsList.value = itemsList
//        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<Story>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class StoryHolder(val binding: RawStoryBinding) : RecyclerView.ViewHolder(binding.root)
}
