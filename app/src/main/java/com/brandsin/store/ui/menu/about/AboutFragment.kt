package com.brandsin.store.ui.menu.about

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
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

class AboutFragment : BaseHomeFragment(), Observer<Any?>
{
    private lateinit var viewModel: AboutViewModel
    private lateinit var binding: MenuFragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu_fragment_about, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.about_app))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.aboutAdapter.aboutLiveData.observe(viewLifecycleOwner, this)

        try
        {
            val pInfo: PackageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            viewModel.obsVersion.set("${getString(R.string.version_number)} ${pInfo.versionName}")
        }
        catch (e: PackageManager.NameNotFoundException)
        {
            e.printStackTrace()
        }
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        when (it) {
            Codes.OPEN_FACE -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.facebook)
            }
            Codes.OPEN_TWITTER -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.twitter)
            }
            Codes.OPEN_INSTA -> {
                Utils.openInstagram(requireActivity(), viewModel.socialLinks.instagram)
            }
            is AboutItem ->
            {
                when (it.id) {
                    1 -> {
                        findNavController().navigate(R.id.about_to_common_questions)
                    }
                    2 -> {
                        findNavController().navigate(R.id.about_to_rate_app)
                    }
                }
            }
        }
    }
}