package com.brandsin.store.ui.menu.rateapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentRateAppBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.BaseHomeFragment
import es.dmoral.toasty.Toasty

class RateAppFragment : BaseHomeFragment(), Observer<Any?> {

    private lateinit var binding: MenuFragmentRateAppBinding

    private lateinit var rateAppViewModel: RateAppViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.menu_fragment_rate_app, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.rate_app_title))

        rateAppViewModel = ViewModelProvider(this)[RateAppViewModel::class.java]
        binding.viewModel = rateAppViewModel

        rateAppViewModel.mutableLiveData.observe(viewLifecycleOwner, this)
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.RATING_SUCCESS -> {
                val packageName = "com.brandsin.store"
                val uri = Uri.parse("market://details?id=$packageName")
                val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
                try {
                    startActivity(myAppLinkToMarket)
                } catch (e: ActivityNotFoundException) {
                    Toasty.warning(
                        requireActivity(),
                        "Impossible to find an application for the market"
                    ).show()
                }
            }
        }
    }
}