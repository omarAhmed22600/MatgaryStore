package com.brandsin.store.ui.main.addproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.databinding.ItemProductPhotoBinding
import timber.log.Timber

class ProductPhotoAdapter(
    private val addClickListener: OnClickListener, // For adding new images
    private val deleteClickListener: OnClickListener // For deleting existing images
) : ListAdapter<PhotoModel, ProductPhotoAdapter.ProductPhotoVH>(DiffCallback) {

    class ProductPhotoVH(private var binding: ItemProductPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        // Bind data and set click listeners
        fun bind(
            data: PhotoModel,
            addClickListener: OnClickListener,
            deleteClickListener: OnClickListener,
            position: Int
        ) {
            binding.data = data
            binding.executePendingBindings()

            // Set the click listener for adding new images/videos (dummy image click)
            binding.imgThumbnail.setOnClickListener {
                if (data.isPhotoOrVideo == "none") {
                    addClickListener.onClick(data, position)
                }
            }

            // Set the click listener for deleting existing images/videos
            binding.imgDelete.setOnClickListener {
                if (data.isPhotoOrVideo != "none") {
                    deleteClickListener.onClick(data, position)
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PhotoModel>() {
        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPhotoVH {
        val binding = ItemProductPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductPhotoVH(binding)
    }

    override fun onBindViewHolder(holder: ProductPhotoVH, position: Int) {
        val item = getItem(position)
        Timber.e("item -> $item")
        holder.bind(item, addClickListener, deleteClickListener, position)
    }

    // ClickListener interface
    class OnClickListener(val clickListener: (photoModel: PhotoModel,position:Int) -> Unit) {
        fun onClick(photoModel: PhotoModel, position: Int) = clickListener(photoModel,position)
    }
}
