package com.brandsin.store.ui.menu.storeStatistics


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.databinding.ItemLayoutBinding
import timber.log.Timber

class ProductAdapter :
    ListAdapter<Product, ProductAdapter.ProductsVH>(
        DiffCallback
    ) {
    class ProductsVH(private var binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            binding.product = data
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        val binding =
            com.brandsin.store.databinding.ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsVH(binding)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ProductsVH, position: Int) {
        val notification = getItem(position)
        Timber.e("Attempting to bind item in position $position ")
        /* when (notification.type) {
             NotificationType.UPDATE_PROFILE.apiValue -> {
                 holder.itemView.setOnClickListener {
                     EditProfileFragment.startFragment(holder.itemView.findNavControllerOfProject(),true)
                 }
             }
             NotificationType.TEAM_OBSERVATION.apiValue -> {
                 holder.itemView.setOnClickListener {
                     ChooseTeamMemberFragment.startFragment(holder.itemView.findNavControllerOfProject(),ChooseTeamMemberFragment.DAILY_OBSERVATION,0.0,0.0)
                 }
             }
             NotificationType.REPORT.apiValue -> {
                 holder.itemView.setOnClickListener {
                     ReportsFragment.startFragment(holder.itemView.findNavControllerOfProject())
                     holder.itemView.findFragment<NotificationsFragment>().activityViewModel?.changeBotNavBarSelection(R.id.reportsFragment)
                 }
             }
             NotificationType.SALARY.apiValue -> {
                 holder.itemView.setOnClickListener {
                     SalaryAndPaycutsFragment.startFragment(holder.itemView.findNavControllerOfProject())
                 }
             }
             NotificationType.VACATIONS.apiValue -> {
                 holder.itemView.setOnClickListener {
                     VacationsFragment.startFragment(holder.itemView.findNavControllerOfProject())
                 }
             }
             else -> {

             }
         }*/
        holder.bind(notification)
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [MarsProperty]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [MarsProperty]
     */
}

