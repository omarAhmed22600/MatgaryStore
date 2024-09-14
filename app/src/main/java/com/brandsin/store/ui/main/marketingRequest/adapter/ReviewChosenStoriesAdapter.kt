package com.brandsin.store.ui.main.marketingRequest.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemChooseStoryBinding
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible
import com.bumptech.glide.Glide

class ReviewChosenStoriesAdapter :
    RecyclerView.Adapter<ReviewChosenStoriesAdapter.ReviewChoseStoriesViewHolder>() {

    private var storiesItemList: List<StoriesItem?>? = ArrayList()

    private lateinit var binding: ItemChooseStoryBinding

    inner class ReviewChoseStoriesViewHolder(itemView: ItemChooseStoryBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgStory = itemView.imgStory
        val imgChecked = itemView.imgChecked
        val icStoryVideo = itemView.icStoryVideo
        val constraintStoryText = itemView.constraintStoryText
        val storyText = itemView.storyText
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewChoseStoriesViewHolder {
        binding =
            ItemChooseStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewChoseStoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewChoseStoriesViewHolder, position: Int) {
        val storyItemList = storiesItemList!![position]
        // bind view
        bindData(holder, storyItemList!!)
    }

    private fun bindData(
        holder: ReviewChoseStoriesViewHolder,
        storyItem: StoriesItem
    ) {

        if (storyItem.mediaUrl?.endsWith(".mp4") == true) {
            holder.icStoryVideo.visible()
            holder.constraintStoryText.gone()
            holder.storyText.gone()

            Glide.with(holder.itemView.context)
                .load(storyItem.mediaUrl)
                .error(R.drawable.app_logo)
                .into(holder.imgStory)

        } else if (storyItem.mediaUrl.isNullOrEmpty() && storyItem.text?.isNotEmpty() == true) {
            holder.imgStory.gone()
            holder.icStoryVideo.gone()
            holder.constraintStoryText.visible()
            holder.storyText.visible()

            holder.storyText.text = storyItem.text.toString()
        } else if (storyItem.mediaUrl?.isNotEmpty() == true && storyItem.text?.isNotEmpty() == true) {
            holder.imgStory.gone()
            holder.icStoryVideo.gone()
            holder.constraintStoryText.visible()
            holder.storyText.visible()

            holder.storyText.text = storyItem.text.toString()

            Glide.with(holder.itemView.context)
                .load(storyItem.mediaUrl)
                .error(R.drawable.app_logo)
                .into(holder.imgStory)
        } else {
            holder.imgStory.visible()
            holder.icStoryVideo.gone()
            holder.constraintStoryText.gone()
            holder.storyText.gone()

            Glide.with(holder.itemView.context)
                .load(storyItem.mediaUrl)
                .error(R.drawable.app_logo)
                .into(holder.imgStory)
        }

        if (storyItem.isSelected) {
            holder.imgChecked.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_checked)
        }
    }

    override fun getItemCount(): Int {
        return storiesItemList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(storiesList: List<StoriesItem?>?) {
        storiesItemList = storiesList
        notifyDataSetChanged()
    }
}