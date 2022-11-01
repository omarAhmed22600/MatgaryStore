package com.brandsin.store.ui.menu.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentWalletBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.menu.about.AboutItem
import com.brandsin.store.ui.activity.home.HomeActivity

class WalletFragment : Fragment(), Observer<Any?>
{
    lateinit var binding: MenuFragmentWalletBinding
    lateinit var viewModel: WalletViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = MenuFragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(WalletViewModel::class.java)
        binding.viewModel = viewModel

        (requireActivity() as HomeActivity).customizeToolbar(getString(R.string.wallet), true, ContextCompat.getColor(requireActivity(), R.color.payment_color))
        (requireActivity() as HomeActivity).customStatusBar(ContextCompat.getColor(requireActivity(), R.color.payment_color))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        it.let {
            if (it is AboutItem)
            {
                when (it.id) {
                    Codes.SHOW_COMPLETED_ORDERS -> {
                        // do what you want
                    }
                }
            }
            else if (it is Int)
            {
                when (it) {
                    Codes.NOTIFICATION_CLICK -> {
                        findNavController().navigate(R.id.home_to_notifications)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as HomeActivity).customStatusBar(ContextCompat.getColor(requireActivity(), R.color.white))

    }
}

