package com.brandsin.store.ui.main.marketingRequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentMarketingRequestBinding
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.marketingRequest.viewmodel.MarketingRequestViewModel
import org.koin.android.ext.android.bind

class MarketingRequestFragment : BaseHomeFragment() {

    private var _binding: FragmentMarketingRequestBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarketingRequestViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMarketingRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.marketing_requests))

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.cvPinStoryHomePage.setOnClickListener {
            viewModel.pinStoriesType.value = "story_to_home"
            findNavController().navigate(R.id.chooseStoriesMarketingRequestFragment)
        }

        binding.cvPinStoryOffersPage.setOnClickListener {
            viewModel.pinStoriesType.value = "story_to_offers"
            findNavController().navigate(R.id.chooseStoriesMarketingRequestFragment)
        }

        binding.cvPinOfferHomePage.setOnClickListener {
            viewModel.pinOffersType.value = "offer_to_home"
            findNavController().navigate(R.id.chooseStoriesMarketingRequestFragment)
        }
        binding.cvPinOfferOffersPage.setOnClickListener {
            viewModel.pinOffersType.value = "offer_to_offers"
            findNavController().navigate(R.id.chooseStoriesMarketingRequestFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}