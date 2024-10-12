package com.brandsin.store.ui.profile.addedstories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawAddedStoriesBinding
import com.brandsin.store.model.ListStoriesResponse
import com.brandsin.store.model.Story
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItemByDate
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.SingleLiveEvent
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddedStoriesAdapter : RecyclerView.Adapter<AddedStoriesAdapter.AddedStoriesHolder>() {
    var allStories = SingleLiveEvent<ArrayList<Story>>()
    var itemsList: ArrayList<ListStoriesResponse> = ArrayList()
    var deleteStoryData = SingleLiveEvent<Story>()
    var showStoryData = SingleLiveEvent<Story>()

    private var convertDate = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedStoriesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RawAddedStoriesBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.raw_added_stories, parent, false)
        return AddedStoriesHolder(binding)
    }

    override fun onBindViewHolder(holder: AddedStoriesHolder, position: Int) {
        val itemViewModel = ItemAddedStoriesViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        convertDate(itemViewModel.item.date.toString())

        holder.binding.tvTime.text = convertDate

        itemViewModel.storyAdapter.deleteLiveData.observeForever {
            when (it) {
                is Story -> {
                    deleteStoryData.value = it
                }
            }
        }

        itemViewModel.storyAdapter.showLiveData.observeForever {
            when (it) {
                is Story -> {
                    showStoryData.value = it
                }
            }
        }

        itemViewModel.storyAdapter.allItemsList.observeForever {
            this.allStories.value = it!!
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<ListStoriesResponse>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class AddedStoriesHolder(val binding: RawAddedStoriesBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun convertDate(dateString: String): String {
        var date: Date? = null
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        try {
            date = formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val f2: DateFormat = SimpleDateFormat("EEEE, dd-MM-yyyy", Locale(PrefMethods.getLanguage()))
        convertDate = f2.format(date)
        return convertDate
    }
}
