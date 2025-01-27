package com.brandsin.store.ui.menu.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentContactBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.Utils

class ContactFragment : BaseHomeFragment(), Observer<Any?> {

    private lateinit var binding: MenuFragmentContactBinding

    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.menu_fragment_contact, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        binding.viewModel = viewModel

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getPhoneNumber()
            viewModel.getSocialLinks()
        }

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        setBarName(getString(R.string.contact_us))
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.SHOW_SOCIAL -> {
                if (viewModel.socialLinks.facebook == null) {
                    binding.ivFacebook.visibility = View.GONE
                }
                if (viewModel.socialLinks.twitter == null) {
                    binding.ivTwitter.visibility = View.GONE
                }
                /*if (viewModel.socialLinks.pinterest == null) {
                    binding.ivGmail.visibility = View.GONE
                }
                if (viewModel.socialLinks.tikTok == null) {
                    binding.ivTiktok.visibility = View.GONE
                }*/
            }

            Codes.PHONE_CLICKED -> {
                Utils.callPhone(requireActivity(), viewModel.obsPhoneNumber.get().toString())
            }

            Codes.FACE_CLICKED -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.facebook)
            }

            Codes.GMAIL_CLICKED -> {
                Utils.openMail(requireActivity(), "info@brandsin.net")
            }

            Codes.WHATSAPP_CLICKED -> {
                val url = "https://api.whatsapp.com/send?phone=" + viewModel.obsPhoneNumber.get()
                    .toString()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            Codes.TWITTER_CLICKED -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.twitter)
            }

            Codes.TIKTOK_CLICKED -> {
                Utils.openLink(requireActivity(), viewModel.socialLinks.tikTok.toString())
            }
        }
    }
}