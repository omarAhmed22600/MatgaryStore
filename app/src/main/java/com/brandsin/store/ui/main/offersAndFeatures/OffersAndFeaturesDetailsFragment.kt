package com.brandsin.store.ui.main.offersAndFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentOffersAndFeaturesDetailsBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.offersAndFeatures.model.OfferAndFeatureItem
import com.brandsin.store.ui.main.offersAndFeatures.viewmodel.OffersAndFeaturesViewModel
import com.bumptech.glide.Glide

class OffersAndFeaturesDetailsFragment : BaseHomeFragment() {

    private var _binding: FragmentOffersAndFeaturesDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OffersAndFeaturesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOffersAndFeaturesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.offers_and_features))

        viewModel.getOfferAndFeatureById()

        subscribeData()
    }

    private fun subscribeData() {
        viewModel.getOfferAndFeatureByIdResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initView(it.data?.offerAndFeatureItem)
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 2)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                    viewModel.obsIsEmpty.set(false)
                    viewModel.obsIsFull.set(false)
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(false)
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)
                }

                else -> {}
            }
        }
    }

    private fun initView(offerAndFeatureItem: OfferAndFeatureItem?) {
        Glide.with(requireContext())
            .load(offerAndFeatureItem?.thumbnail)
            .error(R.drawable.app_logo)
            .into(binding.imgOfferAndFeature)

        binding.offerAndFeatureName.text = offerAndFeatureItem?.name ?: ""
        binding.offerAndFeatureDate.text = offerAndFeatureItem?.createdAt ?: ""
        binding.offerAndFeatureDesc.text = offerAndFeatureItem?.description ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}