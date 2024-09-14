package com.brandsin.store.ui.auth.verify

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.AuthFragmentVerifyBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.auth.AuthActivity
import com.brandsin.store.ui.activity.auth.BaseAuthFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.PrefMethods

class VerifyFragment : BaseAuthFragment(), Observer<Any?> {
    lateinit var binding: AuthFragmentVerifyBinding
    lateinit var viewModel: VerifyViewModel
    var phone = ""
    var userId = ""
    var fromWhere = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_verify, container, false)
        (requireActivity() as AuthActivity).setBarName(getString(R.string.verification_code))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[VerifyViewModel::class.java]
        binding.viewModel = viewModel

        val secondFragmentArgs: VerifyFragmentArgs =
            VerifyFragmentArgs.fromBundle(requireArguments())
        phone = secondFragmentArgs.phone
        userId = secondFragmentArgs.userId
        fromWhere = secondFragmentArgs.fromWhere
        viewModel.verifyRequest.phone_number = phone
        viewModel.verifyRequest.user_id = userId

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.showProgress().observe(viewLifecycleOwner) { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.CODE_RESENT -> {
                showToast(viewModel.newcode, 2)
                viewModel.setShowProgress(false)
            }

            Codes.CODE_EMPTY -> {
                showToast(getString(R.string.enter_verification_code), 1)
            }

            Codes.CODE_SHORT -> {
                showToast(getString(R.string.short_code), 1)
            }

            Codes.VERIFY_SUCCESS -> {
                if (fromWhere == "forgot") {

                    val action =
                        VerifyFragmentDirections.verifyToReset(
                            viewModel.verifyRequest.phone_number.toString(),
                            viewModel.userId,
                            viewModel.verifyRequest.code.toString()
                        )
                    findNavController().navigate(action)

                } else {
                    when {
                        PrefMethods.getIsAskedToLogin() -> {
                            requireActivity().finish()
                        }

                        else -> {
                            PrefMethods.saveLoginState(true)
                            startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            requireActivity().finishAffinity()
                        }
                    }
                }
                viewModel.setShowProgress(false)
            }

            else -> {
                if (value is String) {
                    showToast(value.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }
}