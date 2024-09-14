package com.brandsin.store.ui.menu.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentWalletBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.menu.about.AboutItem
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.menu.wallet.adapter.WalletAdapter

class WalletFragment : Fragment(), Observer<Any?> {

    private lateinit var binding: MenuFragmentWalletBinding

    lateinit var viewModel: WalletViewModel

    private lateinit var walletAdapter: WalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuFragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[WalletViewModel::class.java]
        binding.viewModel = viewModel

        /*(requireActivity() as HomeActivity).customStatusBar(
            ContextCompat.getColor(
                requireActivity(),
                R.color.payment_color
            )
        )*/

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.getWalletTransactions()

        setBtnListener()
        initRecycler()
        subscribeData()
    }

    private fun initRecycler() {
        binding.rvWallet.apply {
            walletAdapter = WalletAdapter()
            adapter = walletAdapter
        }
    }

    private fun setBtnListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getWalletTransactions()
            binding.swipeRefresh.isRefreshing = false
        }
    }


    private fun subscribeData() {
        viewModel.getWalletTransactions.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    binding.tvWalletBalance.text = it.data?.data?.balanceFormated.toString()

                    walletAdapter.submitData(it.data?.data?.transactions)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    viewModel.obsIsEmpty.set(true)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                }

                is ResponseHandler.StopLoading -> {
                    // stop a progress bar
                    viewModel.obsIsLoading.set(false)
                }

                else -> {}
            }
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            if (it is AboutItem) {
                when (it.id) {
                    Codes.SHOW_COMPLETED_ORDERS -> {
                        // do what you want
                    }
                }
            } else if (it is Int) {
                when (it) {
                    Codes.NOTIFICATION_CLICK -> {
                        findNavController().navigate(R.id.home_to_notifications)
                    }
                }
            }
        }
    }
}

