package com.brandsin.store.ui.main.home.addstory.uploadStoryProoduct

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentUploadStoryProductBinding
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.home.addstory.AddStoryViewModel
import com.brandsin.store.utils.observe
import timber.log.Timber

class UploadStoryProductFragment : BaseHomeFragment() {

    private var _binding: FragmentUploadStoryProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddStoryViewModel by activityViewModels()

    private var offsetX = 0f
    private var offsetY = 0f

    private var xOffset = 0
    private var yOffset = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUploadStoryProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setBtnListener()
        subscribeData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.imgUpload.setImageURI(viewModel.request.file?.toUri())
        binding.productName.text = viewModel.productName.toString()
        binding.productPrice.text =
            viewModel.productSalePrice.toString() + " " + getString(R.string.currency)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setBtnListener() {
        binding.constraintTagXAndY.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    offsetX = binding.constraintTagXAndY.x - event.rawX
                    offsetY = binding.constraintTagXAndY.y - event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    binding.constraintTagXAndY.x = event.rawX + offsetX
                    binding.constraintTagXAndY.y = event.rawY + offsetY
                }

                MotionEvent.ACTION_UP -> {
                    // Get the updated X and Y offsets after the movement
                    xOffset = binding.constraintTagXAndY.left
                    yOffset = binding.constraintTagXAndY.top
                }
            }
            true
        }

        binding.icCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.icShareStory.setOnClickListener {
            viewModel.request.x = xOffset.toString()
            viewModel.request.y = yOffset.toString()
            viewModel.setShowProgress(true)
            viewModel.uploadStories()
        }
    }

    private fun subscribeData() {
        observe(viewModel.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    viewModel.setShowProgress(false)
                    showToast(it.message.toString(), 1)
                }

                Status.SUCCESS_MESSAGE -> {
                    viewModel.setShowProgress(false)
                    showToast(it.message.toString(), 2)
                }

                Status.SUCCESS -> {
                    when (it.data) {
                        is UploadStoryResponse -> {
                            showToast(getString(R.string.uploaded_story_successfully), 2)
                            viewModel.setShowProgress(false)
                            findNavController().navigate(R.id.action_uploadStoryProduct_to_nav_add_story)
                        }
                    }
                }

                else -> {
                    viewModel.setShowProgress(false)
                    Timber.e(it.message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.request.productId = null
        _binding = null
    }
}