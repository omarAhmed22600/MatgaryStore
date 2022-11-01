package com.brandsin.store.ui.auth.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.AuthFragmentSplashBinding
import com.brandsin.store.model.constants.Const
import com.brandsin.store.ui.activity.auth.AuthActivity
import com.brandsin.store.ui.activity.auth.BaseAuthFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseAuthFragment()

{
    lateinit var binding: AuthFragmentSplashBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.auth_fragment_splash,
            container,
            false
        )

        (requireActivity() as AuthActivity).setBarName(getString(R.string.login))
        return binding.root

//        lifecycleScope.launch {
//            delay(2000)
//
//            when {
//                PrefMethods.getLoginState() -> {
//                    requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
//                    requireActivity().finishAffinity()
//                }
//                else -> {
//                    findNavController().navigate(R.id.splash_to_login_ways)
//                }
//            }
//        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val path = "android.resource://" + requireActivity().packageName.toString() + "/" + R.raw.splash

        binding.vedioSplash.setVideoURI(Uri.parse(path),false)

        binding.vedioSplash.start()
        binding.vedioSplash.setOnCompletionListener {
            when {
                PrefMethods.getLoginState() -> {
                    requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finishAffinity()
                }
                else -> {
                    findNavController().navigate(R.id.splash_to_login_ways)
                }
            }
        }
    }
}