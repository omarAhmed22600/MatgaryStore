package com.brandsin.store.ui.profile.addedstories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawAddedStoriesBinding
import com.brandsin.store.model.profile.addedstories.liststories.DataItem
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.SingleLiveEvent
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddedStoriesAdapter : RecyclerView.Adapter<AddedStoriesAdapter.AddedStoriesHolder>() {

    var itemsList: ArrayList<DataItem> = ArrayList()
    var deleteStoryData = SingleLiveEvent<StoriesItem>()
    var showStoryData = SingleLiveEvent<StoriesItem>()
    var convertDate = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedStoriesHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding: RawAddedStoriesBinding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_added_stories, parent, false)
        return AddedStoriesHolder(binding)
    }

    override fun onBindViewHolder(holder: AddedStoriesHolder, position: Int) {
        val itemViewModel = ItemAddedStoriesViewModel(itemsList[position])
        holder.binding.viewModel = itemViewModel

        ConvertDate(itemViewModel.item.date.toString())
        holder.binding.tvTime.text = convertDate

        itemViewModel.storyAdapter.deleteLiveData.observeForever{
            when (it) {
                is StoriesItem -> {
                    deleteStoryData.value = it
                }
            }
        }

        itemViewModel.storyAdapter.showLiveData.observeForever{
            when (it) {
                is StoriesItem -> {
                    showStoryData.value = it
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateList(models: ArrayList<DataItem>) {
        itemsList = models
        notifyDataSetChanged()
    }

    inner class AddedStoriesHolder(val binding: RawAddedStoriesBinding) : RecyclerView.ViewHolder(binding.root)

    fun ConvertDate(dateString: String): String {

        var date: Date? = null
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val temp = dateString
        try {
            date = formatter.parse(temp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val f2: DateFormat = SimpleDateFormat("EEEE, dd-MM-yyyy", Locale(PrefMethods.getLanguage()))
        convertDate = f2.format(date)
        return convertDate
    }
}
