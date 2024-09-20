package com.brandsin.store.ui.menu.storeStatistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentStoreStatisticsBinding
import com.brandsin.store.databinding.MenuFragmentRateAppBinding
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity


class StoreStatisticsFragment : BaseHomeFragment() {
    private lateinit var binding: FragmentStoreStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_store_statistics, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as HomeActivity).customizeToolbar("", false)

    }
}