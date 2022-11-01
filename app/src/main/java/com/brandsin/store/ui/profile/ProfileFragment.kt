package com.brandsin.store.ui.profile

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.ProfileFragmentBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.PrefMethods
import timber.log.Timber
import java.util.*

class ProfileFragment : Fragment(), Observer<Any?>
{
    lateinit var binding : ProfileFragmentBinding
    lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        (requireActivity() as HomeActivity).customizeToolbar(getString(R.string.personal_profile))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.viewModel = viewModel

        if (PrefMethods.getLanguage(requireActivity()) == "ar")
        {
           binding.tvLanguage.text = "English"
        }
        else
        {
            binding.tvLanguage.text = "العربية"
        }

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        binding.ibSwitch.isChecked = PrefMethods.getIsNotificationsEnabled()!!

        binding.notificationLayout.setOnClickListener {
            when {
                binding.ibSwitch.isChecked -> {
                    binding.ibSwitch.isChecked = false
                    PrefMethods.setIsNotificationsEnabled(false)
                }
                else -> {
                    binding.ibSwitch.isChecked = true
                    PrefMethods.setIsNotificationsEnabled(true)
                }
            }
        }
        binding.ibSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                PrefMethods.setIsNotificationsEnabled(true)
            } else {
                PrefMethods.setIsNotificationsEnabled(false)
            }
        }
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        it.let {
            when (it) {
                is Int -> {
                    when (it)
                    {
                        Codes.EDIT_CLICKED ->
                        {
                            findNavController().navigate(R.id.profile_to_update)
                        }
                        Codes.VEHICLE_CLICKED ->
                        {
                            findNavController().navigate(R.id.profile_to_store_info)
                        }
                        Codes.CHANGE_PASS_CLICKED ->
                        {
                            findNavController().navigate(R.id.profile_to_change_pass)
                        }
                        Codes.ADD_STORIES ->
                        {
                            findNavController().navigate(R.id.profile_to_add_stories)
                        }
                        Codes.LANGUAGE_CLICKED ->
                        {
                            if (PrefMethods.getLanguage(requireActivity()) == "ar")
                            {
                                setLanguage("en")
                            }
                            else
                            {
                                setLanguage("ar")
                            }
                        }
                        else ->
                            Timber.e("")
                    }
                }
                else ->
                    Timber.e("")
            }
        }
    }

    private fun setLanguage(language: String?)
    {
        val mainConfig = Configuration(resources.configuration)
        val locale = Locale(language)
        Locale.setDefault(locale)
        mainConfig.setLocale(locale)
        resources.updateConfiguration(mainConfig, null)
        PrefMethods.setLanguage(language.toString())

        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }
}