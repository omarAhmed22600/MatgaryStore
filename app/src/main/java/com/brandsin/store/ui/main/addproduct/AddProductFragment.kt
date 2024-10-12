package com.brandsin.store.ui.main.addproduct

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentAddProductBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.constants.Params
import com.brandsin.store.model.main.products.add.AddProductResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.addproduct.DialogAddProductFragment
import com.brandsin.store.ui.dialogs.productcategories.DialogProductCategoriesFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.fxn.pix.Options
import com.fxn.pix.Pix
import timber.log.Timber
import java.io.File
import java.util.Locale

class AddProductFragment : BaseHomeFragment(), Observer<Any?> {
    lateinit var binding: HomeFragmentAddProductBinding


    lateinit var viewModel: AddProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.rvProductSku.adapter = ProductAttributesAdapter(viewModel)
        /*viewModel.addproductSkuAdapter.value = AddProductSkuAdapter(
            viewModel,
            AddProductSkuAdapter.OnClickListener {
                viewModel.showColorSelectionDialog(requireContext())
            },
            AddProductSkuAdapter.OnClickListener {
                viewModel.showMassSelectionDialog(requireContext())
            },
            AddProductSkuAdapter.OnClickListener {
                viewModel.showCapacitySelectionDialog(requireContext())
            },
        )*/
        binding.rvProductPhotos.adapter = ProductPhotoAdapter(
            ProductPhotoAdapter.OnClickListener { _, _ ->
                // Add new image if dummy image is clicked
                Timber.e("Add image clicked")
                pickImage(Codes.SELECT_PHOTO1, Options.Mode.All)
            },
            ProductPhotoAdapter.OnClickListener { _, itemPosition ->
                Timber.e("Delete image clicked")

                var newPosition = itemPosition
                // Ensure the position is valid before proceeding
                val currentListSize = viewModel.imageList.value!!.size

                // Adjust position if it's out of bounds
                if (newPosition >= currentListSize) {
                    newPosition = currentListSize - 1 // Reset to the last valid newPosition
                }

                // Now check if the newPosition is valid for deletion
                if (newPosition >= 0 && newPosition < currentListSize) {
                    // Update image list
                    val tempImageList = viewModel.imageList.value
                    tempImageList?.let {
                        // Remove the item at the specified newPosition
                        it.removeAt(newPosition)
                        // Add a dummy item back to maintain consistent list size
                        it.add(
                            it.lastIndex+1,
                            PhotoModel(
                                isPhotoOrVideo = "none",
                                photoOrVideoBitmap = null
                            )
                        )
                        Timber.e("Updated image list: $it")
                    }
                    viewModel.imageList.value = tempImageList
                    // Update file image list
                    val tempFileList = viewModel.fileImageList.value
                    tempFileList?.let {
                        if (newPosition < it.size) {
                            it.removeAt(newPosition) // Remove corresponding file entry
                            Timber.e("Updated file image list: $it")
                        }
                    }
                    viewModel.fileImageList.value = tempFileList

                    // Notify the adapter
                    binding.rvProductPhotos.adapter?.notifyItemChanged(newPosition)
                    binding.rvProductPhotos.adapter?.notifyItemRangeChanged(newPosition, viewModel.imageList.value!!.size - newPosition)
                    Timber.e("newPosition: $newPosition (Size: ${viewModel.fileImageList.value!!.size})\nNew image list: ${viewModel.imageList.value}\nNew file list: ${viewModel.fileImageList.value!!}")
                } else {
                    Timber.e("Invalid position: $newPosition (Size: ${viewModel.imageList.value!!.size})")
                }
            }

        )

        setBarName(getString(R.string.add_product))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

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
                        is AddProductResponse -> {
                            val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.ADD_PRODUCT)
                            Utils.startDialogActivity(
                                requireActivity(),
                                DialogAddProductFragment::class.java.name,
                                Codes.DIALOG_PRODUCT_REQUEST,
                                bundle
                            )
                        }
                    }
                }

                else -> {
                    Timber.e(it.message)
                }
            }
        }


        /**
         * for debugging purpose
         */
        viewModel.imageList.observe(viewLifecycleOwner) {
            Timber.e("image list changed $it")
        }
        viewModel.fileImageList.observe(viewLifecycleOwner) {
            Timber.e("file list changed $it")
        }

        /*viewModel.addproductSkuAdapter.value!!.productSkuLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                viewModel.addProductRequest.skusList = it
                viewModel.validation()
            }
        })*/

/*
        viewModel.addproductSkuAdapter.value!!.productUnitLiveData.observe(viewLifecycleOwner, {
            if (viewModel.addProductRequest.categoriesIds != null) {
                if (it != null) {
                    viewModel.skuPosition = it

                    val bundle = Bundle()
                    bundle.putSerializable(Params.PRODUCT_UNIT, viewModel.unitList)
                    Utils.startDialogActivity(
                        requireActivity(),
                        DialogProductUnitFragment::class.java.name,
                        Codes.DIALOG_PRODUCT_UNIT_CODE,
                        bundle
                    )
                }
            } else {
                showToast(getString(R.string.enter_product_type), 1)
            }
        })
*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("request code wanted is $Codes.DIALOG_PRODUCT_CATEGORY_CODE\nrequest code sent $requestCode \n result code $resultCode\ndata: ${data}")

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    /*Codes.SELECT_PHOTO1 -> {
                        data?.let {
                            // Get the photoURI that you previously set when opening the camera
                            val returnValue = photoURI
                            returnValue?.let { file2 ->
                                binding.rvProductPhotos.visibility = View.VISIBLE
                                binding.photo1Layout.visibility = View.GONE
                                val file = file2 // Convert URI to File object
                                val uri = file.toUri()
                                Timber.e("uri $uri")

                                var bitmap: Bitmap? = null
                                var thumbnail: Bitmap? = null
                                val isVideo: Boolean? = when {
                                    file.extension.lowercase(Locale.ROOT).let { ext ->
                                        ext == "jpg" || ext == "png" || ext == "jpeg"
                                    } -> {
                                        try {
                                            bitmap = when {
                                                // Handle content URI (content://)
                                                uri.scheme.equals("content", ignoreCase = true) -> {
                                                    // Use ContentResolver to open an InputStream for content URIs
                                                    val inputStream = requireActivity().contentResolver.openInputStream(uri)
                                                    BitmapFactory.decodeStream(inputStream)
                                                }
                                                // Handle file URI (file://) or regular file path
                                                uri.scheme.equals("file", ignoreCase = true) || uri.scheme == null -> {
                                                    BitmapFactory.decodeFile(file.absolutePath)
                                                }
                                                else -> null
                                            }

                                            // If bitmap is successfully created
                                            bitmap?.let {
                                                Timber.e("Bitmap created successfully")
                                            } ?: run {
                                                Timber.e("Failed to create bitmap: unsupported URI scheme")
                                            }
                                            // Handle image selection
                                            Timber.e("Image selected")
                                            false // Indicate it's not a video
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            Timber.e("Error converting Uri to Bitmap: ${e.message}")
                                            null
                                        }
                                    }
                                    // Check for video formats
                                    file.extension.lowercase(Locale.ROOT).let { ext ->
                                        ext == "mp4" || ext == "mkv" || ext == "mpeg" ||
                                                ext == "wmv" || ext == "amv" || ext == "mov"
                                    } -> {
                                        Timber.e("Video selected")
                                        val retriever = MediaMetadataRetriever()
                                        retriever.setDataSource(file.path)
                                        thumbnail = retriever.getFrameAtTime(
                                            1000000,
                                            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                                        ) // Get frame at 1 second (in microseconds)
                                        retriever.release()
                                        true // Indicate it's a video
                                    }
                                    else -> {
                                        showToast(getString(R.string.unsupported_format), 1)
                                        null
                                    }
                                }

                                // Determine the position in the image list
                                var position = getEmptyPosition(viewModel.imageList.value.orEmpty())
                                if (viewModel.imageList.value.isNullOrEmpty()) {
                                    position = 0
                                }
                                if ((position == -1 && viewModel.imageList.value!!.size == 10) || position >= 10) {
                                    // No available position or exceeded the limit
                                    showToast(getString(com.fxn.pix.R.string.selection_limiter_pix, 10), 1)
                                    return
                                }

                                // Create the new photo model
                                val newPhotoModel = PhotoModel(
                                    isPhotoOrVideo = when {
                                        isVideo == true -> "video"
                                        isVideo == false -> "photo"
                                        else -> "none"
                                    },
                                    photoOrVideoBitmap = when {
                                        isVideo == true -> thumbnail
                                        isVideo == false -> bitmap
                                        else -> null
                                    }
                                )

                                // Ensure we still have exactly 10 slots (fill with dummies if needed)
                                while (viewModel.imageList.value!!.size < 10) {
                                    viewModel.imageList.value!!.add(
                                        PhotoModel(
                                            isPhotoOrVideo = "none",
                                            photoOrVideoBitmap = null
                                        )
                                    )
                                }

                                // Replace the dummy item at the found position with the new photo or video
                                viewModel.imageList.value = viewModel.imageList.value!!.apply {
                                    removeAt(position) // Remove dummy item
                                    add(position, newPhotoModel) // Add new photo/video at the same position
                                }

                                // Update the file list accordingly
                                viewModel.fileImageList.value = viewModel.fileImageList.value!!.apply {
                                    add(position, file) // Add the new file at the same position
                                }

                                // Notify the adapter about the change
                                binding.rvProductPhotos.adapter?.notifyDataSetChanged()
                            }
                        }
                    }*/
                    Codes.SELECT_PHOTO1 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.rvProductPhotos.visibility = View.VISIBLE
                                binding.photo1Layout.visibility = View.GONE
                                val fileUri = array[0].toUri()

                                val file = File(array[0])
                                Timber.e("uri $fileUri")
                                var bitmap: Bitmap? = null
                                var thumbnail: Bitmap? = null
                                val isVideo =
                                    if (file.extension.toLowerCase(Locale.ROOT).contains("jpg")
                                        || file.extension.toLowerCase(Locale.ROOT).contains("png")
                                        || file.extension.toLowerCase(Locale.ROOT).contains("jpeg")
                                    ) {
                                        try {
                                            bitmap = when {
                                                // Handle content URI (content://)
                                                fileUri.scheme.equals(
                                                    "content",
                                                    ignoreCase = true
                                                ) -> {
                                                    val inputStream =
                                                        requireActivity().contentResolver.openInputStream(
                                                            fileUri
                                                        )
                                                    BitmapFactory.decodeStream(inputStream)
                                                }
                                                // Handle file URI (file://) or regular file path
                                                fileUri.scheme.equals(
                                                    "file",
                                                    ignoreCase = true
                                                ) || fileUri.scheme == null -> {
                                                    BitmapFactory.decodeFile(file.absolutePath)
                                                }

                                                else -> null
                                            }

                                            // If bitmap is successfully created
                                            bitmap?.let {
                                                Timber.e("Bitmap created successfully")
                                            } ?: run {
                                                Timber.e("Failed to create bitmap: unsupported URI scheme")
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            Timber.e("Error converting Uri to Bitmap")
                                        }
                                        // Handle image selection
                                        Timber.e("image")
                                        false
                                    } else if (file.extension.toLowerCase(Locale.ROOT)
                                            .contains("mp4")
                                        || file.extension.toLowerCase(Locale.ROOT).contains("mkv")
                                        || file.extension.toLowerCase(Locale.ROOT).contains("mpeg")
                                        || file.extension.toLowerCase(Locale.ROOT).contains("wmv")
                                        || file.extension.toLowerCase(Locale.ROOT).contains("amv")
                                        || file.extension.toLowerCase(Locale.ROOT).contains("mov")
                                    ) {
                                        // Handle video selection
                                        Timber.e("video")
                                        val videoFile = File(array[0])
                                        val retriever = MediaMetadataRetriever()
                                        retriever.setDataSource(videoFile.path)
                                        thumbnail = retriever.getFrameAtTime(
                                            1000000,
                                            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                                        ) // Get frame at 1 second (in microseconds)
                                        retriever.release()
                                        true
                                    } else {
                                        showToast(
                                            getString(R.string.unsupported_format),
                                            1
                                        )
                                        null
                                    }
                                var position = getEmptyPosition(viewModel.imageList.value.orEmpty())
                                if (viewModel.imageList.value.isNullOrEmpty()) {
                                    position = 0
                                }
                                if ((position == -1 && viewModel.imageList.value!!.size == 10) || position >= 10) {
                                    // No available position or exceeded the limit
                                    showToast(
                                        getString(com.fxn.pix.R.string.selection_limiter_pix, 10),
                                        1
                                    )
                                    return
                                }

                                val newPhotoModel = PhotoModel(
                                    isPhotoOrVideo = if (isVideo == true) "video" else if (isVideo == false) "photo" else "none",
                                    photoOrVideoBitmap = if (isVideo == true) thumbnail else if (isVideo == false) bitmap else null
                                )

// Ensure we still have exactly 10 slots (fill with dummies if needed)
                                while (viewModel.imageList.value!!.size < 10) {
                                    viewModel.imageList.value!!.add(
                                        PhotoModel(
                                            isPhotoOrVideo = "none",
                                            photoOrVideoBitmap = null
                                        )
                                    )
                                }
// Replace the dummy item at the found position with the new photo or video
                                viewModel.imageList.value = viewModel.imageList.value!!.apply {
                                    removeAt(position) // Remove dummy item
                                    add(
                                        position,
                                        newPhotoModel
                                    ) // Add new photo/video at the same position
                                }

// Update the file list accordingly
                                viewModel.fileImageList.value =
                                    viewModel.fileImageList.value!!.apply {
                                        add(position, file) // Add the new file at the same position
                                    }


// Notify the adapter about the change
                                binding.rvProductPhotos.adapter?.notifyDataSetChanged()

                            }
                        }
                    }
                }


                    when {
                        requestCode == Codes.DIALOG_PRODUCT_CATEGORY_CODE && data != null -> {
                            if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                                Timber.e("data: ${data.getStringExtra("productCategoryNames")}")
                                when {
                                    data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                                        binding.productCategory.text =
                                            data.getStringExtra("productCategoryNames")
                                        viewModel.addProductRequest.categoriesIds =
                                            data.getIntegerArrayListExtra("productCategoryId")
                                        viewModel.getUnitsList(viewModel.addProductRequest.categoriesIds!!)
                                    }
                                }
                            }
                        }
                    }

                        when {
                        requestCode == Codes.DIALOG_PRODUCT_UNIT_CODE && data != null -> {
                            if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                                when {
                                    data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                                        var productUnitId =
                                            data.getStringExtra("productUnitId").toString()
                                        var productUnitName =
                                            data.getStringExtra("productUnitName").toString()

                                        viewModel.addproductSkuAdapter.value!!.updateList(
                                            productUnitId,
                                            productUnitName,
                                            viewModel.skuPosition
                                        )
                                    }
                                }
                            }
                        }
                    }

                    when {
                        requestCode == Codes.DIALOG_PRODUCT_REQUEST && data != null -> {
                            when {
                                data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                                    when {
                                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                                            findNavController().navigate(R.id.add_products_to_products)
                                        }

                                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 2 -> {
                                            startActivity(
                                                Intent(
                                                    requireActivity(),
                                                    HomeActivity::class.java
                                                )
                                            )
                                            requireActivity().finishAffinity()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> it.let {
                when (it) {
                    Codes.SELECT_PHOTO1 -> {
                        pickImage(Codes.SELECT_PHOTO1, Options.Mode.All)
                    }

                    Codes.PRODUCT_CATEGORIES_CLICKED -> {
                        val bundle = Bundle()
                        bundle.putSerializable(
                            Params.PRODUCT_CATEGORIES,
                            viewModel.categoriesList
                        )
                        if (viewModel.addProductRequest.categoriesIds != null) {
                            bundle.putSerializable(
                                Params.PRODUCT_CATEGORIES_DATA,
                                viewModel.addProductRequest
                            )
                        }
                        Utils.startDialogActivity(
                            requireActivity(),
                            DialogProductCategoriesFragment::class.java.name,
                            Codes.DIALOG_PRODUCT_CATEGORY_CODE,
                            bundle
                        )
                    }

                    Codes.EMPTY_TYPE -> {
                        showToast(getString(R.string.enter_product_type), 1)
                    }

                    Codes.NAME_EMPTY -> {
                        showToast(getString(R.string.enter_product_name_ar), 1)
                    }

                    Codes.EMPTY_DESCRIPTION -> {
                        showToast(getString(R.string.enter_description_ar), 1)
                    }

                    Codes.EMPTY_PRODUCT_NAME_EN -> {
                        showToast(getString(R.string.enter_product_name_en), 1)
                    }

                    Codes.EMPTY_PRODUCT_DESCRIPTION_EN -> {
                        showToast(getString(R.string.enter_description_en), 1)
                    }

                    Codes.EMPTY_IMAGE -> {
                        showToast(getString(R.string.enter_image), 1)
                    }

                    Codes.EMPTY_SKU -> {
                        showToast(getString(R.string.enter_sku_item), 1)
                    }

                    Codes.EMPTY_SizeName -> {
                        showToast(getString(R.string.enter_product_size_name), 1)
                    }

                    Codes.EMPTY_InventoryValue -> {
                        showToast(getString(R.string.enter_product_inventory_value), 1)
                    }

                    Codes.EMPTY_Price -> {
                        showToast(getString(R.string.enter_product_price), 1)
                    }

                    Codes.EMPTY_CODE -> {
                        showToast(getString(R.string.enter_product_code), 1)
                    }
                }
            }
        }
    }

    private fun getEmptyPosition(imageList: List<PhotoModel>): Int {
        return imageList.indexOfFirst { it.isPhotoOrVideo == "none" }

    }
}