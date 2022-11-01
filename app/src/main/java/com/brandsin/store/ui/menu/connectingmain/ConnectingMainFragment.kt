package com.brandsin.store.ui.menu.connectingmain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentConnectingMainBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.BaseHomeFragment

class ConnectingMainFragment : BaseHomeFragment(), Observer<Any?>
{
    private lateinit var viewModel : ConnectingMainViewModel
    private lateinit var binding: MenuFragmentConnectingMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu_fragment_connecting_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel  = ViewModelProvider(this).get( ConnectingMainViewModel::class.java)
        binding.viewModel = viewModel 
        viewModel .getTxt2()
        setBarName(getString(R.string.connecting_to_the_main_headquarters))

        viewModel .mutableLiveData.observe(viewLifecycleOwner, this)
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        when (it) {
            Codes.EMPTY_STORE_CODE -> {
                showToast(getString(R.string.enter_store_codes), 1)
            }
            true -> {
                showToast(getString(R.string.link_request_successfully), 2)
            }
            else -> {
                showToast(it.toString(), 1)
            }
        }
    }
}