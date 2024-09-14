package com.brandsin.store.ui.main.offersAndFeatures.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemOfferAndFeatureBinding
import com.brandsin.store.ui.main.offersAndFeatures.model.OfferAndFeatureItem
import com.bumptech.glide.Glide

class OffersAndFeaturesAdapter(
    private val btnDetailsClickCallBack: (offerAndFeature: OfferAndFeatureItem) -> Unit,
) : RecyclerView.Adapter<OffersAndFeaturesAdapter.OffersAndFeaturesViewHolder>() {

    private var offerAndFeatureList: List<OfferAndFeatureItem?>? = ArrayList()

    private lateinit var binding: ItemOfferAndFeatureBinding

    inner class OffersAndFeaturesViewHolder(itemView: ItemOfferAndFeatureBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgOfferAndFeature = itemView.imgOfferAndFeature
        val offerAndFeatureName = itemView.offerAndFeatureName
        val offerAndFeatureDate = itemView.offerAndFeatureDate
        val offerAndFeatureDesc = itemView.offerAndFeatureDesc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersAndFeaturesViewHolder {
        binding =
            ItemOfferAndFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OffersAndFeaturesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OffersAndFeaturesViewHolder, position: Int) {
        val item = offerAndFeatureList!![position]
        // bind view
        bindData(holder, item!!)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(
        holder: OffersAndFeaturesViewHolder,
        offerAndFeature: OfferAndFeatureItem
    ) {
        Glide.with(holder.itemView.context)
            .load(offerAndFeature.thumbnail)
            .error(R.drawable.app_logo)
            .into(binding.imgOfferAndFeature)

        holder.offerAndFeatureName.text = offerAndFeature.name ?: ""
        holder.offerAndFeatureDate.text = offerAndFeature.createdAt ?: ""
        holder.offerAndFeatureDesc.text = offerAndFeature.description ?: ""

        holder.itemView.rootView.setOnClickListener {
            btnDetailsClickCallBack.invoke(offerAndFeature)
        }
    }

    override fun getItemCount(): Int {
        return offerAndFeatureList?.size ?: 0
    }

    fun submitData(offerAndFeature: List<OfferAndFeatureItem?>?) {
        offerAndFeatureList = offerAndFeature
        notifyDataSetChanged()
    }
}