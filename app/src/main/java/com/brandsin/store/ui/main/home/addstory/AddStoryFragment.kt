package com.brandsin.store.ui.main.home.addstory

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentAddStoryBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.home.addstory.uploadStoryProoduct.ChooseProductFragment
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.observe
import com.brandsin.store.utils.visible
import com.fxn.pix.Options
import com.fxn.pix.Pix
import timber.log.Timber
import java.io.File

class AddStoryFragment : BaseHomeFragment(), Observer<Any?> {

    private lateinit var binding: HomeFragmentAddStoryBinding

    private val viewModel: AddStoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        setBarName(getString(R.string.add_story))

        viewModel.mutableLiveData.observe(requireActivity(), this)

        // getAllImages()

        setBtnListener()
        subscribeData()
    }

    private fun setBtnListener() {
        binding.constraintUploadStoryProduct.setOnClickListener {
            // type = "product"
            pickImage(9999, Options.Mode.Picture)
        }

        binding.cvTxt.setOnClickListener {
            findNavController().navigate(R.id.add_story_to_add_story_txt)
        }

        binding.cvStoryVideo.setOnClickListener {
            viewModel.uploadType = "video"
            pickImage(Codes.PRODUCT_IMG_REQUEST_CODE, Options.Mode.Video)
        }

        binding.constraintUploadStoryPhoto.setOnClickListener {
            viewModel.uploadType = "photo"
            pickImage(Codes.PRODUCT_IMG_REQUEST_CODE, Options.Mode.Picture)
        }
    }

    private fun subscribeData() {
        /*viewModel.showProgress().observe(viewLifecycleOwner) { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.gone()
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.progressLayout.visible()
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
        }*/

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
                            // binding.progressLayout.gone()
                            viewModel.setShowProgress(false)
                            findNavController().navigate(R.id.add_story_to_add_stories)
                        }
                    }
                }

                else -> {
                    Timber.e(it.message)
                }
            }
        }

        viewModel.galleryAdapter.uploadLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.request.file = it
                viewModel.request.productId = null
                viewModel.request.x = null
                viewModel.request.y = null
                viewModel.setShowProgress(true)
                viewModel.uploadStories()
            }
        }
    }

    override fun onChanged(value: Any?) {
        when (value) {
            null -> return
            else -> value.let {
                /* when (it) {
                     Codes.SELECT_IMAGES -> {
                        viewModel.uploadType = "photo"
                        pickImage(Codes.PRODUCT_IMG_REQUEST_CODE, Options.Mode.Picture)
                    }

                    Codes.SELECT_VIDEO -> {
                        viewModel.uploadType = "video"
                        pickImage(Codes.PRODUCT_IMG_REQUEST_CODE, Options.Mode.Video)
                    }

                    Codes.SELECT_TEXT -> {
                        findNavController().navigate(R.id.nav_add_story_txt)
                    }
                }*/
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Codes.PRODUCT_IMG_REQUEST_CODE -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                viewModel.request.file = File(array[0])
                                viewModel.setShowProgress(true)
                                // viewModel.uploadStories()

                                findNavController().navigate(R.id.previewUploadStoryPhotoAndVideoFragment)
                            }
                        }
                    }

                    9999 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                viewModel.request.file = File(array[0])
                                viewModel.setShowProgress(true)

                                // show bottom sheet to enter reason
                                val bottomSheetFragment = ChooseProductFragment()
                                bottomSheetFragment.show(
                                    childFragmentManager,
                                    "ChooseProductFragment"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // get all images from external storage
    fun getAllImages() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                101
            )
        }

        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val imageSortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor: Cursor? = requireActivity().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            imageSortOrder
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val absolutePathOfImage =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
                viewModel.galleryList.add(absolutePathOfImage)
            }
        } else {
            Log.d("AddViewModel", "Cursor is null!")
        }
        viewModel.galleryAdapter.updateList(viewModel.galleryList)
    }
}