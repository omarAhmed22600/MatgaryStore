package com.brandsin.store.ui.activity.auth.condition

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityConditionBinding
import com.brandsin.store.model.auth.condition.ConditionsResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.ParentActivity
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import timber.log.Timber

class ConditionActivity : ParentActivity(), Observer<Any?>
{
    lateinit var binding : ActivityConditionBinding
    lateinit var viewModel : ConditionViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_condition)
        viewModel = ViewModelProvider(this).get(ConditionViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

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

    override fun onChanged(it: Any?) {
        if(it == null) return
        it.let {
            when (it) {
                Codes.BACK_PRESSED -> {
                    val intent = Intent()
                    finish()
                }
            }
        }
    }
    fun showToast(msg: String, type: Int) {
        val bundle = Bundle()
        bundle.putString(Params.DIALOG_TOAST_MESSAGE, msg)
        bundle.putInt(Params.DIALOG_TOAST_TYPE, type)
        Utils.startDialogActivity(this, DialogToastFragment::class.java.name, Codes.DIALOG_TOAST_REQUEST, bundle)
    }
}