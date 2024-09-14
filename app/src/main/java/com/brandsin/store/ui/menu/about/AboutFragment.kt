package com.brandsin.store.ui.menu.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentAboutBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.menu.about.AboutItem
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.Utils
import es.dmoral.toasty.Toasty

class AboutFragment : BaseHomeFragment(), Observer<Any?> {

    private lateinit var binding: MenuFragmentAboutBinding

    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu_fragment_about, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AboutViewModel::class.java]
        binding.viewModel = viewModel

        setBarName(getString(R.string.about_app))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.aboutAdapter.aboutLiveData.observe(viewLifecycleOwner, this)

        try {
            val pInfo: PackageInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            viewModel.obsVersion.set("${getString(R.string.version_number)} ${pInfo.versionName}")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.OPEN_FACE -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.facebook)
            }

            Codes.OPEN_TWITTER -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.twitter)
            }

            Codes.OPEN_INSTA -> {
                Utils.openInstagram(requireActivity(), viewModel.socialLinks.instagram)
            }

            Codes.TIKTOK_CLICKED -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.tikTok.toString())
            }

            Codes.WHATSAPP_CLICKED -> {
                val url = "https://api.whatsapp.com/send?phone=" + viewModel.obsPhoneNumber.get()
                    .toString()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            is AboutItem -> {
                when (value.id) {
                    1 -> {
                        findNavController().navigate(R.id.about_to_common_questions)
                    }

                    2 -> {
                        // findNavController().navigate(R.id.about_to_rate_app)

                        // open google play store to add rate and review
                        val packageName = "com.brandsin.store"
                        val uri = Uri.parse("market://details?id=$packageName")
                        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
                        try {
                            startActivity(myAppLinkToMarket)
                        } catch (e: ActivityNotFoundException) {
                            Toasty.warning(
                                requireActivity(),
                                "Impossible to find an application for the market"
                            ).show()
                        }
                    }
                }
            }
        }
    }
}