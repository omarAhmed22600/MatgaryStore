package com.brandsin.store.ui.chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.brandsin.store.databinding.ItemLeftMessageBinding
import com.brandsin.store.databinding.ItemRightMessageBinding
import com.brandsin.user.ui.chat.model.MessageModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MessagesAdapter(
    private val imageClickCallBack: (image: String) -> Unit
) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    private var messagesList: MutableList<MessageModel>? = ArrayList()

    inner class MessageViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> {
                val binding = ItemRightMessageBinding.inflate(inflater, parent, false)
                MessageViewHolder(binding)
            }

            0 -> {
                val binding = ItemLeftMessageBinding.inflate(inflater, parent, false)
                MessageViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val message = messagesList!![position]

        holder.setIsRecyclable(false)

        when (viewType) {
            1 -> {
                // Right message layout binding
                val binding = ItemRightMessageBinding.bind(holder.itemView)
// Right message layout binding
                val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

// The original time string, for example
                val originalTime = message.date.orEmpty()

// Parsing the original time string
                val date = dateFormat.parse(originalTime)

// Formatting it to the new format
                val formattedTime = timeFormat.format(date!!)
                binding.messageDateR.text = formattedTime

                // Bind data to views
                if (message.type == "text") {
                    binding.sendImageR.visibility = View.GONE
                    binding.txtMessageR.text = message.message
                } else {
                    binding.txtMessageR.visibility = View.GONE
                    binding.sendImageR.visibility = View.VISIBLE
                    Glide.with(binding.root)
                        .load(message.image.trim())
                        .into(binding.sendImageR)
                    binding.sendImageR.setOnClickListener {
                        imageClickCallBack.invoke(message.image)
                    }
                }
            }

            0 -> {
                // Left message layout binding
                val binding = ItemLeftMessageBinding.bind(holder.itemView)
// Right message layout binding
                val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

// The original time string, for example
                val originalTime = message.date.orEmpty()

// Parsing the original time string
                val date = dateFormat.parse(originalTime)

// Formatting it to the new format
                val formattedTime = timeFormat.format(date!!)
                binding.messageDateL.text = formattedTime
                // Bind data to views
                if (message.type == "text") {
                    binding.sendImageL.visibility = View.GONE
                    binding.txtMessageL.text = message.message
                } else if (message.type == "image") {
                    binding.txtMessageL.visibility = View.GONE
                    binding.sendImageL.visibility = View.VISIBLE
                    Glide.with(binding.root)
                        .load(message.image)
                        .into(binding.sendImageL)
                    binding.sendImageL.setOnClickListener {
                        imageClickCallBack.invoke(message.image)
                    }
                }
            }

            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messagesList!![position]
        return if (message.typeBay == "user") 0
        else 1
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDateSToString(time: Long): String {
        val formatter = SimpleDateFormat("hh.mm aa")

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return formatter.format(calendar.time)
    }

    fun submitData(messages: MutableList<MessageModel>) {
        messagesList = messages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messagesList?.size ?: 0
    }
}