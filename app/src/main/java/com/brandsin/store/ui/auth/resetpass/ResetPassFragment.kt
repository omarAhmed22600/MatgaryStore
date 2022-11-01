package com.brandsin.store.ui.auth.resetpass

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.AuthFragmentResetBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.auth.AuthActivity
import com.brandsin.store.ui.activity.auth.BaseAuthFragment

class ResetPassFragment : BaseAuthFragment(), Observer<Any?>
{
    lateinit var binding : AuthFragmentResetBinding
    lateinit var viewModel : ResetViewModel
    val bundle = Bundle()
    var phone=""
    var userId=""
    var code=""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_reset, container, false)

        (requireActivity() as AuthActivity).setBarName(getString(R.string.reset_password))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResetViewModel::class.java)
        binding.viewModel = viewModel

        val secondFragmentArgs: ResetPassFragmentArgs = ResetPassFragmentArgs.fromBundle(requireArguments())
        phone=secondFragmentArgs.phone
        userId=secondFragmentArgs.userId
        code=secondFragmentArgs.code

        viewModel.request.phone_email = phone
        viewModel.request.user_id = userId
        viewModel.request.code = code

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.showProgress().observe(viewLifecycleOwner, { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        })
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        when (it) {
            Codes.PASSWORD_EMPTY -> {
                showToast(getString(R.string.empty_password) , 1)
            }
            Codes.PASSWORD_SHORT -> {
                showToast(getString(R.string.invalid_password) , 1)
            }
            Codes.CONFIRM_PASS_EMPTY -> {
                showToast(getString(R.string.empty_confirm_password) , 1)
            }
            Codes.PASSWORD_NOT_MATCH -> {
                showToast(getString(R.string.not_matc_passwords) , 1)
            }
            Codes.RESET_SUCCESS ->
            {
                showToast(getString(R.string.successfully_updated) , 2)
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finishAffinity()
                viewModel.setShowProgress(false)
            }
            else -> {
                if (it is String) {
                    showToast(it.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }
}