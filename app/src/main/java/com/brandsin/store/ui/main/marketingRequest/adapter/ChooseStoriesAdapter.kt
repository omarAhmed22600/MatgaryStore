package com.brandsin.store.ui.main.marketingRequest.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemChooseStoryBinding
import com.brandsin.store.model.Story
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible
import com.bumptech.glide.Glide

class ChooseStoriesAdapter(
    private val btnChooseStoryClickCallBack: (storyItem: Story) -> Unit,
) : RecyclerView.Adapter<ChooseStoriesAdapter.ChooseStoriesViewHolder>() {

    private var storiesItemList: List<Story?>? = ArrayList()

    private lateinit var binding: ItemChooseStoryBinding

    inner class ChooseStoriesViewHolder(itemView: ItemChooseStoryBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgStory = itemView.imgStory
        val imgChecked = itemView.imgChecked
        val icStoryVideo = itemView.icStoryVideo
        val constraintStoryText = itemView.constraintStoryText
        val storyText = itemView.storyText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseStoriesViewHolder {
        binding =
            ItemChooseStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChooseStoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseStoriesViewHolder, position: Int) {
        val storyItemList = storiesItemList!![position]
        // bind view
        bindData(holder, storyItemList!!)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun bindData(
        holder: ChooseStoriesViewHolder,
        storyItem: Story
    ) {

        if (storyItem.media_url?.endsWith(".mp4") == true) {
            holder.icStoryVideo.visible()
            holder.constraintStoryText.gone()
            holder.storyText.gone()

            Glide.with(holder.itemView.context)
                .load(storyItem.media_url)
                .error(R.drawable.app_logo)
                .into(holder.imgStory)

        } else if (storyItem.media_url.isNullOrEmpty() && storyItem.text?.isNotEmpty() == true) {
            holder.imgStory.gone()
            holder.icStoryVideo.gone()
            holder.constraintStoryText.visible()
            holder.storyText.visible()

            holder.storyText.text = storyItem.text.toString()
        } else if (storyItem.media_url?.isNotEmpty() == true && storyItem.text?.isNotEmpty() == true) {
            holder.imgStory.gone()
            holder.icStoryVideo.gone()
            holder.constraintStoryText.visible()
            holder.storyText.visible()

            holder.storyText.text = storyItem.text.toString()

            Glide.with(holder.itemView.context)
                .load(storyItem.media_url)
                .error(R.drawable.app_logo)
                .into(holder.imgStory)
        } else {
            holder.imgStory.visible()
            holder.icStoryVideo.gone()
            holder.constraintStoryText.gone()
            holder.storyText.gone()

            Glide.with(holder.itemView.context)
                .load(storyItem.media_url)
                .error(R.drawable.app_logo)
                .into(holder.imgStory)
        }

        holder.itemView.rootView.setOnClickListener {
            if (!storyItem.isSelected) { // storyItem.isSelected == false
                storyItem.isSelected = true
                holder.imgChecked.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_checked)
            } else { // storyItem.isSelected == true
                storyItem.isSelected = false
                holder.imgChecked.background =
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_not_checked
                    )
            }
            btnChooseStoryClickCallBack.invoke(storyItem)
        }
    }

    override fun getItemCount(): Int {
        return storiesItemList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(storiesList: ArrayList<Story>) {
        storiesItemList = storiesList
        notifyDataSetChanged()
    }
}