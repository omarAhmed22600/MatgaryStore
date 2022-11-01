package com.brandsin.store.ui.main.home.acceptconnectingmain

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentAcceptConnectingMainBinding
import com.brandsin.store.model.menu.connectingmain.accept.AcceptConnectingMainResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.observe
import timber.log.Timber

class AcceptConnectingMainFragment : BaseHomeFragment(), Observer<Any?>
{
    private lateinit var viewModelAccept: AcceptConnectingMainViewModel
    private lateinit var binding: HomeFragmentAcceptConnectingMainBinding
    private val fragmentArgs : AcceptConnectingMainFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_accept_connecting_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModelAccept = ViewModelProvider(this).get(AcceptConnectingMainViewModel::class.java)
        binding.viewModel = viewModelAccept
        viewModelAccept.requestId = fragmentArgs.requestId
        viewModelAccept.storeName = fragmentArgs.storeName
        viewModelAccept.getTxt2()
        setBarName("")

        viewModelAccept.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModelAccept.showProgress().observe(viewLifecycleOwner, { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        })

        observe(viewModelAccept.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    showToast(it.message.toString(), 1)
                }
                Status.SUCCESS_MESSAGE -> {
                    showToast(it.message.toString(), 2)
                }
                Status.SUCCESS -> {
                    when (it.data) {
                        is AcceptConnectingMainResponse -> {
                            viewModelAccept.setShowProgress(false)
                            startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            requireActivity().finishAffinity()
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        when (it) {
//            Codes.EMPTY_STORE_CODE -> {
//                showToast(getString(R.string.enter_store_codes), 1)
//            }
//            true -> {
//                showToast(getString(R.string.link_request_successfully), 2)
//            }
//            else -> {
//                showToast(it.toString(), 1)
//            }
        }
    }
}