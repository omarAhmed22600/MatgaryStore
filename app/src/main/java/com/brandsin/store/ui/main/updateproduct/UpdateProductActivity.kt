package com.brandsin.store.ui.main.updateproduct

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityUpdateProductBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.model.main.products.update.SkuUpdateProductItem
import com.brandsin.store.model.main.products.update.UpdateProductResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.addproduct.DialogAddProductFragment
import com.brandsin.store.ui.dialogs.productcategories.DialogProductCategoriesFragment
import com.brandsin.store.ui.dialogs.productunit.DialogProductUnitFragment
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import com.brandsin.store.ui.activity.BaseFragment
import com.brandsin.store.utils.map.PermissionUtil
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class UpdateProductActivity : AppCompatActivity(), Observer<Any?> {
    lateinit var binding: ActivityUpdateProductBinding
    lateinit var viewModel: UpdateProductViewModel
    var productData = ProductsItem()
    var categoriesNames: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_product)
        viewModel = ViewModelProvider(this).get(UpdateProductViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

        binding.ibBack.setOnClickListener {
            finish()
        }

        when {
            intent.hasExtra(Params.PRODUCT_ITEM) -> {
                when {
                    intent.getSerializableExtra(Params.PRODUCT_ITEM) != null -> {
                        productData =
                            intent.extras!!.getSerializable(Params.PRODUCT_ITEM) as ProductsItem

                        viewModel.updateProductRequest.id = productData.id
                        viewModel.updateProductRequest.name = productData.name
                        viewModel.updateProductRequest.description = productData.description
                        viewModel.updateProductRequest.nameEn = productData.nameEn
                        viewModel.updateProductRequest.descriptionEn = productData.descriptionEn
                        viewModel.updateProductRequest.type = productData.type
                        viewModel.updateProductRequest.storeId = productData.storeId
                        productData.categories!!.forEach {
                            viewModel.updateProductRequest.categoriesIds!!.add(
                                it!!.id!!.toInt()
                            )
                        }
                        productData.categories!!.forEach { categoriesNames.add(it!!.name.toString()) }
                        binding.productCategory.text = categoriesNames.joinToString { it -> "$it" }
                        viewModel.updateProductRequest.skusList =
                            productData.skus as ArrayList<SkuUpdateProductItem>
                        viewModel.getUnitsList(viewModel.updateProductRequest.categoriesIds!!)
                        viewModel.updateProductSkuAdapter.setData(
                            viewModel.updateProductRequest.skusList as ArrayList<SkuUpdateProductItem>,
                            viewModel.unitList
                        )

                        if (productData.imagesIds?.size!! >= 1) {
                            binding.notPhoto1.visibility = View.GONE
                            binding.ivImg1.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![0]?.url)
                                .into(binding.ivPhoto1)
                            if (viewModel.updateProductRequest.mediaId == null) {
                                viewModel.updateProductRequest.mediaId = ArrayList()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![0]?.id!!)
                        }
                        if (productData.imagesIds!!.size >= 2) {
                            binding.notPhoto2.visibility = View.GONE
                            binding.ivImg2.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![1]?.url)
                                .into(binding.ivPhoto2)
                            if (viewModel.updateProductRequest.mediaId == null) {
                                viewModel.updateProductRequest.mediaId = ArrayList()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![1]?.id!!)
                        }

                        if (productData.imagesIds!!.size >= 3) {
                            binding.notPhoto3.visibility = View.GONE
                            binding.ivImg3.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![2]?.url)
                                .into(binding.ivPhoto3)
                            if (viewModel.updateProductRequest.mediaId == null) {
                                viewModel.updateProductRequest.mediaId = ArrayList()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![2]?.id!!)
                        }

                        if (productData.imagesIds!!.size >= 4) {
                            binding.notPhoto4.visibility = View.GONE
                            binding.ivImg4.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![3]?.url)
                                .into(binding.ivPhoto4)
                            if (viewModel.updateProductRequest.mediaId == null) {
                                viewModel.updateProductRequest.mediaId = ArrayList()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![3]?.id!!)
                        }
                    }
                }
            }
        }

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
                        is UpdateProductResponse -> {
                            val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.UPDATE_PRODUCT)
                            Utils.startDialogActivity(
                                this,
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

        viewModel.updateProductSkuAdapter.productSkuLiveData.observe(this, {
            if (it != null) {
                viewModel.updateProductRequest.skusList = it
                viewModel.validation()
            }
        })

        viewModel.updateProductSkuAdapter.productUnitLiveData.observe(this, {
            if (viewModel.updateProductRequest.categoriesIds != null) {
                if (it != null) {
                    viewModel.skuPosition = it

                    val bundle = Bundle()
                    bundle.putSerializable(Params.PRODUCT_UNIT, viewModel.unitList)
                    Utils.startDialogActivity(
                        this,
                        DialogProductUnitFragment::class.java.name,
                        Codes.DIALOG_PRODUCT_UNIT_CODE,
                        bundle
                    )
                }
            } else {
                showToast(getString(R.string.enter_product_type), 1)
            }
        })
    }

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> it.let {
                when (it) {
                    Codes.SELECT_PHOTO1 -> {
                        pickImage(Codes.SELECT_PHOTO1, Options.Mode.Picture)
                    }

                    Codes.SELECT_PHOTO2 -> {
                        pickImage(Codes.SELECT_PHOTO2, Options.Mode.Picture)
                    }

                    Codes.SELECT_PHOTO3 -> {
                        pickImage(Codes.SELECT_PHOTO3, Options.Mode.Picture)
                    }

                    Codes.SELECT_PHOTO4 -> {
                        pickImage(Codes.SELECT_PHOTO4, Options.Mode.Picture)
                    }

                    Codes.PRODUCT_CATEGORIES_CLICKED -> {
                        val bundle = Bundle()
                        bundle.putSerializable(Params.PRODUCT_CATEGORIES, viewModel.categoriesList)
                        if (viewModel.updateProductRequest.categoriesIds != null) {
                            bundle.putSerializable(
                                Params.PRODUCT_CATEGORIES_DATA_EDIT,
                                viewModel.updateProductRequest
                            )
                        }
                        Utils.startDialogActivity(
                            this,
                            DialogProductCategoriesFragment::class.java.name,
                            Codes.DIALOG_PRODUCT_CATEGORY_CODE,
                            bundle
                        )
                    }

                    Codes.EMPTY_TYPE -> {
                        showToast(getString(R.string.enter_type), 1)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                            Timber.e("fileUri?.let { $uri")
                            val mimeType = this?.contentResolver?.getType(fileUri)
                            val extension =
                                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                            val fileName = "file_${System.currentTimeMillis()}.$extension"
                            val inputStream = this?.contentResolver?.openInputStream(uri)
                            val file = File(this.cacheDir, fileName)
                            val outputStream = FileOutputStream(file)
                            inputStream?.copyTo(outputStream)
                            outputStream.close()
                            file
                        } ?: run {
                            showToast(getString(R.string.someThing_went_wrong), 1)
                            return
                        }

                        var bitmap: Bitmap? = null
                        binding.notPhoto1.visibility = View.GONE
                        binding.ivImg1.visibility = View.VISIBLE
                        binding.ivPhoto1.setImageURI(fileUri)
                        if (viewModel.updateProductRequest.mediaId != null) {
                            if (viewModel.updateProductRequest.deleteMediaId == null) {
                                viewModel.updateProductRequest.deleteMediaId = ArrayList()
                            }
                            if (viewModel.updateProductRequest.mediaId!!.size >= 3) {
                                viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![2])
                                viewModel.updateProductRequest.mediaId!!.removeAt(2)
                            }
                        } else {
                            viewModel.updateProductRequest.mediaId = ArrayList()
                        }
                        viewModel.uploadRequest.collection = "marketplace-product-gallery"
                        viewModel.uploadRequest.image = file
                        viewModel.upload(0)
                    }

                    Codes.SELECT_PHOTO2 -> {
                        val fileUri: Uri? =
                            if (tempFileUri == null)
                                data?.data
                            else
                                tempFileUri
                        tempFileUri = null
                        Timber.e("file uri is $fileUri")
                        val file = fileUri?.let { uri ->
                            Timber.e("fileUri?.let { $uri")
                            val mimeType = this?.contentResolver?.getType(fileUri)
                            val extension =
                                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                            val fileName = "file_${System.currentTimeMillis()}.$extension"
                            val inputStream = this?.contentResolver?.openInputStream(uri)
                            val file = File(this.cacheDir, fileName)
                            val outputStream = FileOutputStream(file)
                            inputStream?.copyTo(outputStream)
                            outputStream.close()
                            file
                        } ?: run {
                            showToast(getString(R.string.someThing_went_wrong), 1)
                            return
                        }

                        var bitmap: Bitmap? = null
                        binding.notPhoto2.visibility = View.GONE
                        binding.ivImg2.visibility = View.VISIBLE
                        binding.ivPhoto2.setImageURI(fileUri)
                        if (viewModel.updateProductRequest.mediaId != null) {
                            if (viewModel.updateProductRequest.deleteMediaId == null) {
                                viewModel.updateProductRequest.deleteMediaId = ArrayList()
                            }
                            if (viewModel.updateProductRequest.mediaId!!.size >= 4) {
                                viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![3])
                                viewModel.updateProductRequest.mediaId!!.removeAt(3)
                            }
                        } else {
                            viewModel.updateProductRequest.mediaId = ArrayList()
                        }
                        viewModel.uploadRequest.collection = "marketplace-product-gallery"
                        viewModel.uploadRequest.image = file
                        viewModel.upload(1)
                    }

                    Codes.SELECT_PHOTO3 -> {
                        val fileUri: Uri? =
                            if (tempFileUri == null)
                                data?.data
                            else
                                tempFileUri
                        tempFileUri = null
                        Timber.e("file uri is $fileUri")
                        val file = fileUri?.let { uri ->
                            Timber.e("fileUri?.let { $uri")
                            val mimeType = this?.contentResolver?.getType(fileUri)
                            val extension =
                                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                            val fileName = "file_${System.currentTimeMillis()}.$extension"
                            val inputStream = this?.contentResolver?.openInputStream(uri)
                            val file = File(this.cacheDir, fileName)
                            val outputStream = FileOutputStream(file)
                            inputStream?.copyTo(outputStream)
                            outputStream.close()
                            file
                        } ?: run {
                            showToast(getString(R.string.someThing_went_wrong), 1)
                            return
                        }

                        binding.notPhoto3.visibility = View.GONE
                        binding.ivImg3.visibility = View.VISIBLE
                        binding.ivPhoto3.setImageURI(fileUri)
                        if (viewModel.updateProductRequest.mediaId != null) {
                            if (viewModel.updateProductRequest.deleteMediaId == null) {
                                viewModel.updateProductRequest.deleteMediaId = ArrayList()
                            }
                            if (viewModel.updateProductRequest.mediaId!!.size >= 5) {
                                viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![4])
                                viewModel.updateProductRequest.mediaId!!.removeAt(4)
                            }
                        } else {
                            viewModel.updateProductRequest.mediaId = ArrayList()
                        }
                        viewModel.uploadRequest.collection = "marketplace-product-gallery"
                        viewModel.uploadRequest.image = file
                        viewModel.upload(2)


                    }

                    Codes.SELECT_PHOTO4 -> {
                        val fileUri: Uri? =
                            if (tempFileUri == null)
                                data?.data
                            else
                                tempFileUri
                        tempFileUri = null
                        Timber.e("file uri is $fileUri")
                        val file = fileUri?.let { uri ->
                            Timber.e("fileUri?.let { $uri")
                            val mimeType = this?.contentResolver?.getType(fileUri)
                            val extension =
                                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                            val fileName = "file_${System.currentTimeMillis()}.$extension"
                            val inputStream = this?.contentResolver?.openInputStream(uri)
                            val file = File(this.cacheDir, fileName)
                            val outputStream = FileOutputStream(file)
                            inputStream?.copyTo(outputStream)
                            outputStream.close()
                            file
                        } ?: run {
                            showToast(getString(R.string.someThing_went_wrong), 1)
                            return
                        }

                        var bitmap: Bitmap? = null
                        binding.notPhoto4.visibility = View.GONE
                        binding.ivImg4.visibility = View.VISIBLE
                        binding.ivPhoto4.setImageURI(fileUri)
                        if (viewModel.updateProductRequest.mediaId != null) {
                            if (viewModel.updateProductRequest.deleteMediaId == null) {
                                viewModel.updateProductRequest.deleteMediaId = ArrayList()
                            }
                            if (viewModel.updateProductRequest.mediaId!!.size >= 6) {
                                viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![5])
                                viewModel.updateProductRequest.mediaId!!.removeAt(5)
                            }
                        } else {
                            viewModel.updateProductRequest.mediaId = ArrayList()
                        }
                        viewModel.uploadRequest.collection = "marketplace-product-gallery"
                        viewModel.uploadRequest.image = file
                        viewModel.upload(3)
                    }
                }
            }
        }

        when {
            requestCode == Codes.DIALOG_PRODUCT_CATEGORY_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            binding.productCategory.text =
                                data.getStringExtra("productCategoryNames")
                            viewModel.updateProductRequest.categoriesIds =
                                data.getIntegerArrayListExtra("productCategoryId")
                            viewModel.getUnitsList(viewModel.updateProductRequest.categoriesIds!!)
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
                            var productUnitId = data.getStringExtra("productUnitId").toString()
                            var productUnitName = data.getStringExtra("productUnitName").toString()

                            viewModel.updateProductSkuAdapter.updateList(
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
                                val intent = Intent()
                                intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                                setResult(Codes.UPDATE_PRODUCT, intent)
                                finish()
                            }

                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 2 -> {
                                startActivity(Intent(this, HomeActivity::class.java))
                                finishAffinity()
                            }
                        }
                    }
                }
            }
        }
    }

    fun pickImage(requestCode: Int, selectedMode: Options.Mode) {
        Timber.e("pick image called")


        // Start the Pix activity if permissions are granted
        if (hasPermission(permissionsToRequest.toTypedArray())) {
            Timber.e("permission granted")

            // Create a new PixFragment instance
            when (selectedMode) {
                Options.Mode.All -> {
                    showDialogToChooseMediaType(requestCode)
                }

                Options.Mode.Picture -> {
                    showDialogToChooseSourceForPicture(requestCode)
                }

                Options.Mode.Video -> {
                    showDialogToChooseSourceForVideo(requestCode)
                }

                else -> {
                    showToast(getString(R.string.someThing_went_wrong), 1)
                }
            }
        } else {
            Timber.e("not all permissions granted")
            // Handle permission request result in the onRequestPermissionsResult
            // This is handled in the checkAndRequestPermissions method
            checkAndRequestAllPermissions()
        }
    }

    private fun showDialogToChooseMediaType(requestCode: Int) {
        val options = arrayOf(getString(R.string.photo), getString(R.string.video))

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.photo_or_video))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showDialogToChooseSourceForPicture(requestCode) // Picture
                    1 -> showDialogToChooseSourceForVideo(requestCode)   // Video
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Method to show dialog to choose source for Picture
    private fun showDialogToChooseSourceForPicture(requestCode: Int) {
        val options = arrayOf(getString(R.string.gallery), getString(R.string.camera))

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.photo))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGalleryForImage(requestCode) // Gallery
                    1 -> openCameraForImage(requestCode)  // Camera
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Method to show dialog to choose source for Video
    private fun showDialogToChooseSourceForVideo(requestCode: Int) {
        val options = arrayOf(getString(R.string.gallery), getString(R.string.camera))

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.video))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGalleryForVideo(requestCode) // Gallery
                    1 -> openCameraForVideo(requestCode)  // Camera
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Helper methods to open gallery or camera

    var tempFileUri: Uri? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("photoURI", tempFileUri)
    }

    fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap {
        val input = context.contentResolver.openInputStream(selectedImage) ?: return img
        val ei = ExifInterface(input)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270f)
            else -> img
        }
    }

    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val aspectRatio = width.toFloat() / height.toFloat()
        var newWidth = maxWidth
        var newHeight = maxHeight

        if (width > height) {
            newHeight = (newWidth / aspectRatio).toInt()
        } else {
            newWidth = (newHeight * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun rotateImage(img: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    }


    // Helper function to create image file
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    // Helper function to create video file
    @Throws(IOException::class)
    private fun createVideoFile(): File? {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File.createTempFile("MP4_${timeStamp}_", ".mp4", storageDir)
    }

    private fun openGalleryForImage(requestCode: Int) {
        // Implement logic to open gallery for picking an image
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    private fun openGalleryForVideo(requestCode: Int) {
        // Implement logic to open gallery for picking a video
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        startActivityForResult(intent, requestCode)
    }

    private fun openCameraForImage(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile() // Create a file to save the image
        photoFile?.let {
            tempFileUri = FileProvider.getUriForFile(this, "${this.packageName}.provider", it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
            startActivityForResult(
                intent,
                requestCode
            ) // Use the requestCode passed to this function
        }
    }

    private fun openCameraForVideo(requestCode: Int) {
        // Implement logic to open camera for capturing a video
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val videoFile = createVideoFile() // Create a file to save the video
        videoFile?.also {
            tempFileUri = FileProvider.getUriForFile(this, "${this.packageName}.provider", it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
            startActivityForResult(intent, requestCode)
        }
    }

    private val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )

    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )

        } else {

            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )

        }
    }

    private fun checkAndRequestAllPermissions() {
        // Request permissions if any are missing
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                BaseFragment.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun hasPermission(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BaseFragment.REQUEST_CODE_PERMISSIONS) {
            if (hasPermission(permissionsToRequest.toTypedArray())) {
                // Permission granted, continue with your task
                showToast(getString(R.string.permission_granted_try_again), 2)
            } else {
                // Permission denied
                // Check if the user has denied the permission without checking "Don't ask again"
                val shouldShowRationale = permissions.any {
                    ActivityCompat.shouldShowRequestPermissionRationale(this, it)
                }

                if (shouldShowRationale) {
                    // Show a rationale dialog or Toast message explaining why the permission is needed
                    showToast(getString(R.string.permissions_are_needed_to_access_your_media), 1)

                    // Request permissions again
                    requestPermissions(permissions, BaseFragment.REQUEST_CODE_PERMISSIONS)
                } else {
                    // The user has selected "Don't ask again". You might want to disable the feature or show a message.
                    showToast(
                        getString(R.string.permissions_denied_please_enable_them_in_settings),
                        1
                    )
                }
            }
        }
    }

    private fun showToast(msg: String, type: Int) {
        val bundle = Bundle()
        bundle.putString(Params.DIALOG_TOAST_MESSAGE, msg)
        bundle.putInt(Params.DIALOG_TOAST_TYPE, type)
        Utils.startDialogActivity(
            this,
            DialogToastFragment::class.java.name,
            Codes.DIALOG_TOAST_REQUEST,
            bundle
        )
    }
}