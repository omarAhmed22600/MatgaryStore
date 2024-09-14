package com.brandsin.store.ui.auth.login

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
import com.brandsin.store.databinding.AuthFragmentLoginBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.auth.BaseAuthFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.PrefMethods
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class LoginFragment : BaseAuthFragment(), Observer<Any?> {

    private lateinit var binding: AuthFragmentLoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        setBarName(getString(R.string.login))

        viewModel.showProgress().observe(viewLifecycleOwner) { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
        }

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result.token
                viewModel.deviceTokenRequest.token = token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
            })
        // [END retrieve_current_token]
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.EMPTY_PHONE -> {
                showToast(getString(R.string.please_enter_your_phone), 1)
            }

            Codes.INVALID_PHONE -> {
                showToast(getString(R.string.invalid_phone), 1)
            }

            Codes.PASSWORD_EMPTY -> {
                showToast(getString(R.string.empty_password), 1)
            }

            Codes.PASSWORD_SHORT -> {
                showToast(getString(R.string.invalid_password), 1)
            }

            Codes.REGISTER_INTENT -> {
                findNavController().navigate(R.id.login_to_register)
            }

            Codes.FORGOT_INTENT -> {
                findNavController().navigate(R.id.login_to_forgot)
            }

            Codes.LOGIN_SUCCESS -> {
                when {
                    PrefMethods.getIsAskedToLogin() -> {
                        requireActivity().finish()
                    }

                    else -> {
                        requireActivity().finishAffinity()
                        viewModel.setShowProgress(false)
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finishAffinity()
                    }
                }
            }

            Codes.SKIP_CLICKED -> {
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finishAffinity()
            }

            else -> {
                if (value is String) {
                    showToast(value.toString(), 1)

                    /* if (it == "not activate") {
                         showToast(viewModel.code , 2)
                         val action = LoginFragmentDirections.loginToVerify(viewModel.request.email.toString(),viewModel.userID,"login")
                         findNavController().navigate(action)
                     } else {
                         showToast(it.toString(), 1)
                     }*/
                }
                viewModel.setShowProgress(false)
            }
        }
    }
}