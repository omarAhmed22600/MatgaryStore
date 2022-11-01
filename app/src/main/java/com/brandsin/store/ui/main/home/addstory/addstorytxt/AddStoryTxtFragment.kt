package com.brandsin.store.ui.main.home.addstory.addstorytxt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentAddStoryTxtBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.observe
import timber.log.Timber

class AddStoryTxtFragment : BaseHomeFragment(), Observer<Any?>
{
    lateinit var binding : HomeFragmentAddStoryTxtBinding
    lateinit var viewModel : AddStoryTxtViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HomeFragmentAddStoryTxtBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AddStoryTxtViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.add_story))

        viewModel.mutableLiveData.observe(this, this)
        viewModel.showProgress().observe(viewLifecycleOwner, { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        })
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
                        is UploadStoryResponse -> {
                            viewModel.setShowProgress(false)
                            findNavController().navigate(R.id.add_story_txt_to_add_stories)
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        when (it) {
            Codes.ADD_STORIES -> {
                if (binding.etStoryTxt.text.toString().trim().isEmpty()){
                    showToast(getString(R.string.must_write), 1)
                }else{
                    viewModel.request.text = binding.etStoryTxt.text.toString()
                    viewModel.setShowProgress(true)
                    viewModel.uploadStoriesWithout()
                }
            }
            else -> {
                viewModel.setShowProgress(false)
                if (it is String) {
                    showToast(it.toString(), 1)
                }
            }
        }
    }
}