package com.brandsin.store.ui.chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemInboxBinding
import com.brandsin.store.utils.gone
import com.brandsin.user.ui.chat.model.MessageModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

class InboxAdapter(
    private val inboxClickCallBack: (message: MessageModel) -> Unit
) :
    RecyclerView.Adapter<InboxAdapter.InboxViewHolder>(), Filterable {

    private lateinit var binding: ItemInboxBinding

    private var inboxList: MutableList<MessageModel> = ArrayList()
    private var originalInboxList: MutableList<MessageModel> = ArrayList()

    class InboxViewHolder(itemView: ItemInboxBinding) : RecyclerView.ViewHolder(itemView.root) {
        val txtInboxUserName = itemView.txtInboxUserName
        val imgInboxUserImage = itemView.imgInboxUserImage
        val txtInboxLastMessage = itemView.txtInboxLastMessage
        val txtInboxTime = itemView.txtInboxTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        binding = ItemInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InboxViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        val inbox = inboxList[position]

        holder.txtInboxUserName.text = inbox.senderName
        val thisList = inboxList.filter { it.senderId == inbox.senderId && it.storeId == inbox.storeId }
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

        // Sort the list of messages by date
        val sortedMessages = thisList.sortedBy { dateFormat.parse(it.date) }.toMutableList()
        Timber.e("list $sortedMessages")
        Glide.with(holder.itemView.context)
            .load(inbox.avateruser.trim())
            .apply(RequestOptions().circleCrop())
            .into(holder.imgInboxUserImage)

        if (inbox.type == "text") {
            holder.txtInboxLastMessage.text = sortedMessages.last().message
        } else if (inbox.type == "image") {
            holder.txtInboxLastMessage.text = holder.itemView.context.getString(R.string.photo)
        }
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

// The original time string, for example
        val originalTime = inbox.date.orEmpty()

// Parsing the original time string
        val date = dateFormat.parse(originalTime)

// Formatting it to the new format
        val formattedTime = timeFormat.format(date!!)
//        val date = getTimeAgo(holder.itemView.context, inbox.date.toLong())
        holder.txtInboxTime.text = formattedTime


        binding.root.setOnClickListener {
            inboxClickCallBack.invoke(inbox)
        }
        binding.progress.gone()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(inboxes: MutableList<MessageModel>) {
        inboxList = inboxes
        originalInboxList = inboxes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return inboxList.size
    }

    private val SECOND_MILLIS: Int = 1000
    private val MINUTE_MILLIS: Int = 60 * SECOND_MILLIS
    private val HOUR_MILLIS: Int = 60 * MINUTE_MILLIS
    private val DAY_MILLIS: Int = 24 * HOUR_MILLIS

    private fun getTimeAgo(context: Context, time: Long): String? {
        var timeMillis = time

        if (timeMillis < 1000000000000L) {
            timeMillis *= 1000
        }
        val now = System.currentTimeMillis()
        if (timeMillis > now || timeMillis <= 0) {
            return null
        }

        val diff = now - timeMillis

        return when {
            diff < MINUTE_MILLIS -> {
                context.getString(R.string.just_now)
            }

            diff < 2 * MINUTE_MILLIS -> {
                context.getString(R.string.a_minute_ago)
            }

            diff < 50 * MINUTE_MILLIS -> {
                (diff / MINUTE_MILLIS).toString() + context.getString(R.string.minutes_ago)
            }

            diff < 90 * MINUTE_MILLIS -> {
                context.getString(R.string.an_hour_ago)
            }

            diff < 24 * HOUR_MILLIS -> {
                (diff / HOUR_MILLIS).toString() + context.getString(R.string.hours_ago)
            }

            diff < 48 * HOUR_MILLIS -> {
                context.getString(R.string.yesterday)
            }

            else -> {
                (diff / DAY_MILLIS).toString() + context.getString(R.string.days_ago)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString =
                    constraint?.toString()?.lowercase(Locale.getDefault()).toString().trim()

                val filteredList = mutableListOf<MessageModel>()

                if (queryString.isEmpty()) {
                    filteredList.addAll(originalInboxList)
                } else {
                    for (story in originalInboxList) {
                        if (story.storename.lowercase().contains(queryString)) {
                            filteredList.add(story)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                inboxList = results?.values as ArrayList<MessageModel>
                notifyDataSetChanged()
            }
        }
    }
}