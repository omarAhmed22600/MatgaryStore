package com.brandsin.store.ui.menu.help.helpques

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentHelpBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.Status
import com.brandsin.store.network.observe
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.user.model.menu.help.HelpQuesItem
import timber.log.Timber

class HelpFragment : BaseHomeFragment(), Observer<Any?>
{
    private lateinit var viewModel: HelpViewModel
    private lateinit var binding: MenuFragmentHelpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu_fragment_help, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelpViewModel::class.java)
        binding.viewModel = viewModel

       setBarName(getString(R.string.help))

        viewModel.helpAdapter.helpLiveData.observe(viewLifecycleOwner, this)

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getHelpQues()
            viewModel.getPhoneNumber()
        }

        observe(viewModel.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    showToast(it.message.toString(), 1)
                }
                Status.SUCCESS_MESSAGE -> {
                    showToast(it.message.toString(), 2)
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }
    }

    override fun onChanged(it: Any?) {
        if(it == null) return
        when (it) {
            is HelpQuesItem -> {
                val action = HelpFragmentDirections.helpToAnswers(it, viewModel.phoneNumber.toString())
                findNavController().navigate(action)
            }
            Codes.EMPTY_MESSAGE -> {
                showToast(getString(R.string.please_enter_your_message), 1)
            }
        }
    }
}