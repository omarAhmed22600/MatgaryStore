package com.brandsin.store.utils.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

class RecyclerViewsBinding {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter", "app:disableItemChangedAnimation")
        fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?, disableItemAnimation: Boolean) {
            adapter?.let {
                recyclerView.adapter = it
                if (disableItemAnimation) {
                    if (recyclerView.itemAnimator != null) (recyclerView.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations =
                            false
                }
            }
        }
        //put more here





    }
}