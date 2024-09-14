package com.brandsin.store.ui.main.offersAndFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentOffersAndFeaturesBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.offersAndFeatures.adapter.OffersAndFeaturesAdapter
import com.brandsin.store.ui.main.offersAndFeatures.model.OfferAndFeatureItem
import com.brandsin.store.ui.main.offersAndFeatures.viewmodel.OffersAndFeaturesViewModel

class OffersAndFeaturesFragment : BaseHomeFragment() {

    private var _binding: FragmentOffersAndFeaturesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OffersAndFeaturesViewModel by activityViewModels()

    private lateinit var offersAndFeaturesAdapter: OffersAndFeaturesAdapter

    private val btnDetailsClickCallBack: (offerAndFeatureItem: OfferAndFeatureItem) -> Unit =
        { offerAndFeatureItem ->
            viewModel.offerAndFeatureId = offerAndFeatureItem.id
            findNavController().navigate(R.id.offersAndFeaturesDetailsFragment)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOffersAndFeaturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.offers_and_features))

        viewModel.getAllOffersAndFeatures()

        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun setBtnListener() {
        binding.root.setOnRefreshListener {
            viewModel.getAllOffersAndFeatures()
            binding.root.isRefreshing = false
        }
    }

    private fun initRecycler() {
        binding.rvOfferAndFeature.apply {
            offersAndFeaturesAdapter = OffersAndFeaturesAdapter(btnDetailsClickCallBack)
            adapter = offersAndFeaturesAdapter
        }
    }

    private fun subscribeData() {
        viewModel.getAllOffersAndFeaturesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    offersAndFeaturesAdapter.submitData(it.data?.offerAndFeatureItem)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}