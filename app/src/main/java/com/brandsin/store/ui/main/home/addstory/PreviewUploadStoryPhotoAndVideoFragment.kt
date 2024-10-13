package com.brandsin.store.ui.main.home.addstory

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentPreviewUploadStoryPhotoAndVideoBinding
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.observe
import com.brandsin.store.utils.setImageBitmap
import com.brandsin.store.utils.visible
import timber.log.Timber

class PreviewUploadStoryPhotoAndVideoFragment : BaseHomeFragment() {

    private var _binding: FragmentPreviewUploadStoryPhotoAndVideoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddStoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            FragmentPreviewUploadStoryPhotoAndVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setBtnListener()
        subscribeData()
    }

    private fun initView() {
        if (viewModel.uploadType == "video") {
            binding.uploadVideo.visible()
            binding.uploadPhoto.gone()

            startVideoView()
        } else if (viewModel.uploadType == "photo") {
            binding.uploadVideo.gone()
            binding.uploadPhoto.visible()
            Timber.e("init view ${viewModel.request.file?.toUri()}\n${viewModel.imageBitmap}")
            if (viewModel.imageBitmap != null) {
                Timber.e("image bitmap not null")
                binding.uploadPhoto.setImageBitmap(viewModel.imageBitmap)
                viewModel.imageBitmap = null
            } else {
                Timber.e("image bitmap null")
                binding.uploadPhoto.setImageURI(viewModel.request.file?.toUri())
            }

        }
    }

    private fun startVideoView() {
        // Set the video path (replace "your_video_url" with your actual video URL or resource)
        val videoUri = Uri.parse(viewModel.request.file?.toString())

        // Create a MediaController to enable play, pause, etc.
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.uploadVideo)
        binding.uploadVideo.setMediaController(mediaController)

        // Add a callback to ensure the surface is ready
        binding.uploadVideo.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                // Set the video URI and start playing when the surface is ready
                binding.uploadVideo.setVideoURI(videoUri)
                binding.uploadVideo.start()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Handle surface changes if needed
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                // Release resources if the surface is destroyed
            }
        })
    }


    private fun setBtnListener() {
        binding.icCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.icShareStory.setOnClickListener {
            binding.progressBar.visible()
            viewModel.uploadStories()
        }
    }

    private fun subscribeData() {
        observe(viewModel.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    binding.progressBar.gone()
                    showToast(it.message.toString(), 1)
                }

                Status.SUCCESS_MESSAGE -> {
                    binding.progressBar.gone()
                    showToast(it.message.toString(), 2)
                }

                Status.SUCCESS -> {
                    when (it.data) {
                        is UploadStoryResponse -> {
                            showToast(getString(R.string.uploaded_story_successfully), 2)
                            binding.progressBar.gone()
                            findNavController().navigate(R.id.action_previewUploadStoryPhotoAndVideo_to_nav_add_story)
                        }
                    }
                }

                else -> {
                    // showToast(it.message.toString(), 1)
                    Timber.e("message == ${it.message}")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}