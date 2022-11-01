package com.brandsin.store.ui.auth.condition

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.AuthFragmentConditionBinding
import com.brandsin.store.model.auth.condition.ConditionsResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.auth.BaseAuthFragment
import com.brandsin.store.utils.observe
import timber.log.Timber

class ConditionFragment : BaseAuthFragment()
{
    lateinit var binding : AuthFragmentConditionBinding
    lateinit var viewModel : ConditionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_condition, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConditionViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.terms_and_conditions))

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
                        is ConditionsResponse -> {
                            binding.loginText.text = Html.fromHtml(Html.fromHtml(it.data.data).toString())
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }
    }
}