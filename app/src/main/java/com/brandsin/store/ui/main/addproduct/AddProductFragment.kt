package com.brandsin.store.ui.main.addproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentAddProductBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.constants.Params
import com.brandsin.store.model.main.products.add.AddProductResponse
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.model.main.products.update.SkuUpdateProductItem
import com.brandsin.store.model.main.products.update.UpdateProductResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.addproduct.DialogAddProductFragment
import com.brandsin.store.ui.dialogs.productcategories.DialogProductCategoriesFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.fxn.pix.Options
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList
import java.util.Locale
import kotlin.coroutines.resume

class AddProductFragment : BaseHomeFragment(), Observer<Any?> {
    lateinit var binding: HomeFragmentAddProductBinding

    private val args: AddProductFragmentArgs by navArgs()
    var productData = ProductsItem()
    lateinit var viewModel: AddProductViewModel
    var categoriesNames: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.rvProductSku.adapter = ProductAttributesAdapter(viewModel, viewLifecycleOwner)
        binding.clearSelection.setOnClickListener {
            val adapter = binding.rvProductSku.adapter
            binding.rvProductSku.adapter = null // Temporarily detach the adapter
            binding.rvProductSku.adapter = adapter // Re-attach it, forcing view rebuilding
            viewModel.attributes.value = listOf()

        }
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
                            it.lastIndex + 1,
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
                    binding.rvProductPhotos.adapter?.notifyItemRangeChanged(
                        newPosition,
                        viewModel.imageList.value!!.size - newPosition
                    )
                    Timber.e("newPosition: $newPosition (Size: ${viewModel.fileImageList.value!!.size})\nNew image list: ${viewModel.imageList.value}\nNew file list: ${viewModel.fileImageList.value!!}")
                } else {
                    Timber.e("Invalid position: $newPosition (Size: ${viewModel.imageList.value!!.size})")
                }
            }

        )
        viewModel.imageList.observe(viewLifecycleOwner) {
            Timber.e("image list changed $it")
        }
        viewModel.fileImageList.observe(viewLifecycleOwner) {
            Timber.e("file list changed $it")
        }
        viewModel.attributes.observe(viewLifecycleOwner) {
            Timber.e("attributes changed to :$it")
        }

        if (args.productItem != null) {
            binding.btnAdd.text = getString(R.string.update)
            viewModel.isUpdateProduct.value = true
            productData = args.productItem!!
            Timber.e("productData: $productData")
            viewModel.addProductRequest.id = productData.id
            viewModel.addProductRequest.name = productData.name
            viewModel.addProductRequest.description = productData.description
            viewModel.addProductRequest.nameEn = productData.nameEn
            viewModel.addProductRequest.descriptionEn = productData.descriptionEn
            /*      viewModel.addProductRequest.name = productData.name
                  viewModel.addProductRequest.description = productData.description
                  viewModel.addProductRequest.nameEn = productData.nameEn
                  viewModel.addProductRequest.descriptionEn = productData.descriptionEn*/
            viewModel.updateProductRequest.type = productData.type
            viewModel.addProductRequest.type = productData.type
            viewModel.addProductRequest.storeId = productData.storeId
            productData.categories.orEmpty()
                .forEach { viewModel.addProductRequest.categoriesIds!!.add(it?.id ?: -1) }
            productData.categories.orEmpty().forEach { categoriesNames.add(it!!.name.toString()) }
            binding.productCategory.text = categoriesNames.joinToString { it -> "$it" }
            viewModel.getUnitsList(viewModel.addProductRequest.categoriesIds!!)
            val productImages = productData.images.orEmpty()
            val productVideos = productData.videos.orEmpty()

            Timber.e("videos $productVideos and photos $productImages")
            loadMedia(productImages, productVideos)
        }
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




        setBarName(
            if (viewModel.isUpdateProduct.value == true) getString(R.string.update_product) else getString(
                R.string.add_product
            )
        )

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

                        is UpdateProductResponse -> {
                            showToast(getString(R.string.successfully_updated), 2)
                            findNavController().navigateUp()
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

    fun bitmapToFile(bitmap: Bitmap, fileName: String): File {
        // Create a file in the cache directory
        val file = File(requireContext().cacheDir, fileName)

        var quality = 100
        var fileSize: Long

        // Compress the bitmap and write it to the file with decreasing quality if the size is above 1 MB
        do {
            file.outputStream().use { fileOutputStream ->
                // Compress the bitmap, you can adjust the quality (0-100)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            }
            fileSize = file.length()
            quality -= 5 // Reduce the quality by 5 in each iteration
        } while (fileSize > 1024 * 1024 && quality > 0) // Continue until the file is under 1 MB or quality reaches 0

        return file
    }


    // Function to load both images and videos sequentially
    fun loadMedia(productImages: List<String?>, productVideos: List<String?>) {
        lifecycleScope.launch {
            viewModel.obsIsLoading.set(true)
            // First load all the images
            loadAllImages(productImages)

            // Once images are loaded, load the videos
            loadAllVideos(productVideos)
            val tmpList = viewModel.imageList.value
            // Ensure we still have exactly 10 slots (fill with dummies if needed)
            while (tmpList.orEmpty().size < 10) {
                tmpList!!.add(
                    PhotoModel(
                        isPhotoOrVideo = "none",
                        photoOrVideoBitmap = null
                    )
                )
            }
            viewModel.imageList.postValue(tmpList)
            // Notify the adapter that the data has changed
            binding.rvProductPhotos.adapter?.notifyDataSetChanged()
            viewModel.obsIsLoading.set(false)

        }
    }

    suspend fun loadAllImages(productImages: List<String?>) {
        val tempList = viewModel.imageList.value?.toMutableList() ?: mutableListOf()
        val tempFileList = viewModel.fileImageList.value?.toMutableList() ?: mutableListOf()

        productImages.orEmpty().forEach { imageUrl ->
            val bitmap = withContext(Dispatchers.IO) {
                getBitmapFromUrl(requireContext(), imageUrl.orEmpty())
            }
            bitmap?.let {
                Timber.e("bitmap is $it")

                val photoModel = PhotoModel("photo", it)
                tempList.add(photoModel)

                // Create file from bitmap
                val file = bitmapToFile(it, "image_${System.currentTimeMillis()}.jpg")
                tempFileList.add(file)
            }
        }

        // Update the lists in ViewModel after images are loaded
        viewModel.imageList.value = tempList
        viewModel.fileImageList.value = tempFileList

        Timber.e("Final image list: $tempList")
    }

    suspend fun loadAllVideos(productVideos: List<String?>) {
        // Get the current lists
        val tempList = viewModel.imageList.value?.toMutableList() ?: mutableListOf()
        val tempFileList = viewModel.fileImageList.value?.toMutableList() ?: mutableListOf()

        productVideos.orEmpty().forEach { videoUri ->
            var thumbnail: Bitmap? = null
            val retriever = MediaMetadataRetriever()

            try {
                // Set the data source for the retriever
                retriever.setDataSource(videoUri.orEmpty(), HashMap<String, String>())

                // Retrieve a frame at the 1st second (1000000 microseconds = 1 second)
                thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                } else {
                    retriever.frameAtTime // Retrieve the default frame (usually the first frame)
                }

            } catch (e: Exception) {
                Timber.e(e, "Failed to retrieve thumbnail for %s", videoUri)
            }

            // If a thumbnail is retrieved, add it to the list
            if (thumbnail != null) {
                val photoModel = PhotoModel("video", thumbnail)
                tempList.add(photoModel)
            }

            // Convert video URI to a File object (video itself, not the thumbnail)
            if (!videoUri.isNullOrEmpty()) {
                val videoFile = File(Uri.parse(videoUri).path ?: "")
                if (videoFile.exists()) {
                    Timber.e("video file exists :$videoFile")
                    tempFileList.add(videoFile)
                } else {
                    Timber.e("Video file for %s does not exist.", videoUri)
                }
            }
        }

        // Update ViewModel lists
        viewModel.imageList.value = tempList
        viewModel.fileImageList.value = tempFileList
    }


    suspend fun getBitmapFromUrl(context: Context, url: String): Bitmap? =
        suspendCancellableCoroutine { cont ->
            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        cont.resume(resource) // Resume with bitmap
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        cont.resume(null) // Resume with null if failed
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Clear resources if needed
                    }
                })
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("request code wanted is $Codes.DIALOG_PRODUCT_CATEGORY_CODE\nrequest code sent $requestCode \n result code $resultCode\ndata: ${data}")

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {

                    Codes.SELECT_PHOTO1 -> {
                        val fileUri: Uri? =
                            if (tempFileUri == null)
                                data?.data
                            else
                                tempFileUri
                        tempFileUri = null
                        Timber.e("file uri is $fileUri")
                        val file = fileUri?.let { uri ->
                            val mimeType = context?.contentResolver?.getType(fileUri)
                            val extension =
                                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                            val fileName = "file_${System.currentTimeMillis()}.$extension"
                            val inputStream = context?.contentResolver?.openInputStream(uri)
                            val file = File(requireContext().cacheDir, fileName)
                            val outputStream = FileOutputStream(file)
                            inputStream?.copyTo(outputStream)
                            outputStream.close()
                            file
                        }
                        Timber.e("image uri is :$fileUri \n file is $file")
                        if (file == null) {
                            showToast(getString(R.string.someThing_went_wrong), 1)
                            return
                        }
                        var bitmap: Bitmap? = null
                        var thumbnail: Bitmap? = null
                        val isVideo: Boolean? = when {
                            file.extension.lowercase(Locale.ROOT).let { ext ->
                                ext == "jpg" || ext == "png" || ext == "jpeg"
                            } -> {
                                try {
                                    bitmap = when {
                                        // Handle content URI (content://)
                                        fileUri.scheme.equals("content", ignoreCase = true) -> {
                                            // Use ContentResolver to open an InputStream for content URIs
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
                                        // Resize the bitmap to a manageable size
                                        val resizedBitmap = resizeBitmap(
                                            it,
                                            1024,
                                            1024
                                        ) // Adjust maxWidth and maxHeight as needed

                                        // Rotate the resized bitmap if required
                                        bitmap = rotateImageIfRequired(
                                            requireContext(),
                                            resizedBitmap,
                                            fileUri
                                        )
                                        Timber.e("Bitmap created and processed successfully")
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
                                try {
                                    retriever.setDataSource(context, fileUri)
                                    thumbnail = retriever.getFrameAtTime(
                                        1000000,
                                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                                    ) // Get frame at 1 second (in microseconds)
                                } catch (e: IllegalArgumentException) {
                                    showToast(
                                        getString(
                                            R.string.invalid_uri_or_file_path,
                                            e.message
                                        ), 1
                                    )
                                    return
                                } catch (e: SecurityException) {
                                    showToast(getString(R.string.permission_denied, e.message), 1)
                                    return
                                } catch (e: RuntimeException) {
                                    showToast(
                                        getString(
                                            R.string.failed_to_set_data_source,
                                            e.message
                                        ), 1
                                    )
                                    return
                                } finally {
                                    retriever.release()
                                }
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
                        var quality = 100
                        var fileSize: Long
                        // Compress the bitmap and write it to the file with decreasing quality if the size is above 1 MB
                        if (isVideo!!.not()) {
                            do {
                                file.outputStream().use { fileOutputStream ->
                                    // Compress the bitmap, you can adjust the quality (0-100)
                                    bitmap!!.compress(
                                        Bitmap.CompressFormat.JPEG,
                                        quality,
                                        fileOutputStream
                                    )
                                }
                                fileSize = file.length()
                                quality -= 5 // Reduce the quality by 5 in each iteration
                            } while (fileSize > 1024 * 1024 && quality > 0) // Continue until the file is under 1 MB or quality reaches 0
                        }
                        // Update the file list accordingly
                        viewModel.fileImageList.value = viewModel.fileImageList.value!!.apply {
                            add(position, file) // Add the new file at the same position
                        }
                        // Notify the adapter about the change
                        binding.rvProductPhotos.adapter?.notifyDataSetChanged()
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

            Activity.RESULT_CANCELED -> {
                if (requestCode == Codes.SELECT_PHOTO1) {
                    showToast(getString(R.string.someThing_went_wrong), 1)
                    return
                }
            }

            Codes.DIALOG_OFFER_REQUEST -> {
                data.let {
                    val result = it!!.getIntExtra(Params.DIALOG_CLICK_ACTION, -1)
                    if (result == 1) {
                        findNavController().navigate(R.id.nav_my_products)
                    } else if (result == 2) {
                        findNavController().navigate(R.id.nav_home)
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

                    Codes.ONE_PRICE_ONLY -> {
                        showToast(getString(R.string.please_choose_either_the_attribute_price_or_general_price), 1)
                    }
                    Codes.Edit_NOW -> {
                        showToast(getString(R.string.cant_change_the_product_type_from_simple_to_variable_or_vise_versa),1)
                    }
                }
            }
        }
    }

    private fun getEmptyPosition(imageList: List<PhotoModel>): Int {
        return imageList.indexOfFirst { it.isPhotoOrVideo == "none" }

    }
}