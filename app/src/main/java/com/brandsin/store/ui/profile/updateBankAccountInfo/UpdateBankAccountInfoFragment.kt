package com.brandsin.store.ui.profile.updateBankAccountInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentUpdateBankAccountInfoBinding
import com.brandsin.store.model.profile.updatestore.UpdateStoreResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.observe
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class UpdateBankAccountInfoFragment : BaseHomeFragment(), Observer<Any?> {

    private var _binding: FragmentUpdateBankAccountInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpdateBankAccountInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdateBankAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.categories))

        viewModel = ViewModelProvider(this)[UpdateBankAccountInfoViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        initView()
        subscribeData()
    }

    private fun initView() {
        val storeData = PrefMethods.getStoreData()
        viewModel.updateRequest.storeOwnerName = storeData?.storeOwnerName.toString()
        viewModel.updateRequest.storeBankName = storeData?.storeBankName.toString()
        viewModel.updateRequest.storeIban = storeData?.storeIban.toString()
    }

    private fun subscribeData() {
        observe(viewModel.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    showToast(it.message.toString(), 1)
                }

                Status.SUCCESS_MESSAGE -> {
                    showToast(it.message.toString(), 2)
                }

                Status.SUCCESS -> {
                    when (it.data) {
                        is UpdateStoreResponse -> {
                            showToast(getString(R.string.store_update_successfully), 2)
                            lifecycleScope.launch {
                                delay(2000)
                                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            }
                        }
                    }
                }

                else -> {
                    Timber.e(it.message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            when (it) {

                is String -> {
                    showToast(it.toString(), 1)
                }
            }
        }
    }

}