package com.brandsin.store.ui.menu.coomonquestions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.RawCommonQuestionBinding
import com.brandsin.store.model.menu.commonquest.CommonQuestionItem
import org.jsoup.Jsoup

class CommonQuesAdapter : RecyclerView.Adapter<CommonQuesAdapter.AboutQuesHolder>() {

    private var questionsList: ArrayList<CommonQuestionItem> = ArrayList()
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutQuesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RawCommonQuestionBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.raw_common_question, parent, false)
        return AboutQuesHolder(binding)
    }

    override fun onBindViewHolder(holder: AboutQuesHolder, position: Int) {
        val itemViewModel = ItemCommonQuestionViewModel(questionsList[position])
        holder.binding.viewModel = itemViewModel

        holder.binding.tvAnswer.text = Jsoup.parse(itemViewModel.item.content.toString()).text() ?: ""

        holder.setSelected()

        holder.binding.rawLayout.setOnClickListener {
            when {
                position != selectedPosition -> {
                    notifyItemChanged(position)
                    when {
                        selectedPosition != -1 -> {
                            notifyItemChanged(selectedPosition)
                        }
                    }
                    selectedPosition = position
                }
            }
        }
    }

    fun getItem(position: Int): CommonQuestionItem {
        return questionsList[position]
    }

    override fun getItemCount(): Int {
        return questionsList.size
    }

    fun updateList(models: ArrayList<CommonQuestionItem>) {
        questionsList = models
        notifyDataSetChanged()
    }

    inner class AboutQuesHolder(val binding: RawCommonQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setSelected() {
            when (selectedPosition) {
                adapterPosition -> {
                    binding.tvAnswer.visibility = View.VISIBLE
                }

                else -> {
                    binding.tvAnswer.visibility = View.GONE
                }
            }
        }
    }
}
