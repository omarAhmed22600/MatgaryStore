package com.brandsin.store.ui.auth.register.storeinfo

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
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityRegisterStoreInfoBinding
import com.brandsin.store.model.UserLocation
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.auth.condition.ConditionActivity
import com.brandsin.store.ui.activity.map.MapsActivity
import com.brandsin.store.ui.dialogs.storeDeliveryType.DeliveryTypeDialog
import com.brandsin.store.ui.dialogs.storetags.DialogStoreTagsFragment
import com.brandsin.store.ui.dialogs.storetypes.DialogStoreTypesFragment
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import com.brandsin.store.utils.map.PermissionUtil
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import java.io.File
import com.brandsin.store.ui.activity.BaseFragment
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class RegisterStoreInfoActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var binding: ActivityRegisterStoreInfoBinding
    lateinit var viewModel: RegisterStoreInfoViewModel

    private var storeCategoryNames = ArrayList<String>()
    private var storeTagsNames = ArrayList<String>()

    private val btnChooseTypeOrderCallback: (type: String) -> Unit =
        { type ->
            when (type) {
                getString(R.string.minute) -> {
                    viewModel.storeRequest.storeDeliveryType = getString(R.string.minute)
                    viewModel.obsType.set(getString(R.string.minute))
                }

                getString(R.string.hour) -> {
                    viewModel.storeRequest.storeDeliveryType = getString(R.string.hour)
                    viewModel.obsType.set(getString(R.string.hour))
                }

                getString(R.string.day) -> {
                    viewModel.storeRequest.storeDeliveryType = getString(R.string.day)
                    viewModel.obsType.set(getString(R.string.day))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_store_info)

        viewModel = ViewModelProvider(this)[RegisterStoreInfoViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.mutableLiveData.observe(this, this)

        viewModel.getStoreTypes()

        viewModel.storeRequest.hasDelivery = "1"

        binding.rgDeliveryType.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.minute -> {
                    viewModel.obsType.set(getString(R.string.minute))
                }

                R.id.day -> {
                    viewModel.obsType.set(getString(R.string.day))
                }

                else -> {
                    viewModel.obsType.set(getString(R.string.hour))
                }
            }
        }

        when {
            intent.hasExtra(Params.STORE_REGISTER) -> {
                when {
                    intent.getSerializableExtra(Params.STORE_REGISTER) != null -> {
                        val storeData: StoreRegister =
                            intent.extras!!.getSerializable(Params.STORE_REGISTER) as StoreRegister
                        viewModel.storeRequest = storeData

                        binding.notStoreImg.visibility = View.GONE
                        binding.ivStorePhoto.visibility = View.VISIBLE
                        Glide.with(this).load(storeData.storeThumbUri).into(binding.ivStoreImg)

                        binding.notThumbImg.visibility = View.GONE
                        binding.ivThumb.visibility = View.VISIBLE
                        Glide.with(this).load(storeData.storeImgUri).into(binding.ivStoreThumb)
                    }
                }
            }
        }

        if (viewModel.storeRequest.pickUpFromStore == "1") {
            viewModel.storeRequest.pickUpFromStore = "1"
            binding.yesPickUpFromStore.isChecked = true
            binding.noPickUpFromStore.isChecked = false
        } else {
            viewModel.storeRequest.pickUpFromStore = "0"
            binding.yesPickUpFromStore.isChecked = false
            binding.noPickUpFromStore.isChecked = true
        }

        if (viewModel.storeRequest.cashOnDelivery == "1") {
            viewModel.storeRequest.cashOnDelivery = "1"
            binding.yesCashOnDelivery.isChecked = true
            binding.noCashOnDelivery.isChecked = false
        } else {
            viewModel.storeRequest.cashOnDelivery = "0"
            binding.yesCashOnDelivery.isChecked = false
            binding.noCashOnDelivery.isChecked = true
        }

        if (viewModel.storeRequest.checkDelivery == true) {
            viewModel.storeRequest.hasDelivery = "1"
            binding.yes.isChecked = true
            binding.no.isChecked = false
            binding.priceLayout.visibility = View.VISIBLE
            binding.spaceLayout.visibility = View.VISIBLE
            binding.timeLayout.visibility = View.VISIBLE
        } else {
            viewModel.storeRequest.hasDelivery = "0"
            binding.yes.isChecked = false
            binding.no.isChecked = true
            binding.priceLayout.visibility = View.GONE
            binding.spaceLayout.visibility = View.GONE
            binding.timeLayout.visibility = View.GONE
        }

        binding.rgPickUpFromStore.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.toString() == getString(R.string.yes)) {
                viewModel.storeRequest.pickUpFromStore = "1"
            } else if (radio.text.toString() == getString(R.string.no)) {
                viewModel.storeRequest.pickUpFromStore = "0"
            }
        }

        binding.rgCashOnDelivery.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.toString() == getString(R.string.yes)) {
                viewModel.storeRequest.cashOnDelivery = "1"
            } else if (radio.text.toString() == getString(R.string.no)) {
                viewModel.storeRequest.cashOnDelivery = "0"
            }
        }

        binding.rgDelivery.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.toString() == resources.getString(R.string.yes)) {
                viewModel.storeRequest.checkDelivery = true
                viewModel.storeRequest.hasDelivery = "1"
                binding.priceLayout.visibility = View.VISIBLE
                binding.spaceLayout.visibility = View.VISIBLE
                binding.timeLayout.visibility = View.VISIBLE

                binding.timeLayout.visibility = View.VISIBLE

                checkDeliveryType()

            } else if (radio.text.toString() == resources.getString(R.string.no)) {
                viewModel.storeRequest.checkDelivery = false
                viewModel.storeRequest.hasDelivery = "0"
                binding.priceLayout.visibility = View.GONE
                binding.spaceLayout.visibility = View.GONE
                binding.timeLayout.visibility = View.GONE

                viewModel.storeRequest.storeDeliveryType = null
                viewModel.obsType.set(null)
            }
        }

        checkDeliveryType()

        if (viewModel.storeRequest.checkCondition == true) {
            binding.checkBox.isChecked = true
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.storeRequest.checkCondition = isChecked
        }

        binding.deliveryType.setOnClickListener {
            val deliveryTypeDialog = DeliveryTypeDialog(btnChooseTypeOrderCallback)
            deliveryTypeDialog.show(supportFragmentManager, "DeliveryTypeDialog")
        }
    }

    private fun checkDeliveryType() {
        when (viewModel.storeRequest.storeDeliveryType) {
            getString(R.string.minute) -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.minute)
                viewModel.obsType.set(getString(R.string.minute))
            }

            getString(R.string.hour) -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.hour)
                viewModel.obsType.set(getString(R.string.hour))
            }

            getString(R.string.day) -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.day)
                viewModel.obsType.set(getString(R.string.day))
            }

            else -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.minute)
                viewModel.obsType.set(getString(R.string.minute))
            }
        }
    }

    override fun onChanged(value: Any?) {
        when (value) {
            null -> return
            else -> value.let {
                when (it) {
                    Codes.STORE_TYPE_SUCSSES -> {
                        when {
                            intent.hasExtra(Params.STORE_REGISTER) -> {
                                when {
                                    intent.getSerializableExtra(Params.STORE_REGISTER) != null -> {
                                        val storeData: StoreRegister =
                                            intent.extras!!.getSerializable(Params.STORE_REGISTER) as StoreRegister

                                        viewModel.storeRequest.categories = storeData.categories
                                        for (item in viewModel.typesList) {
                                            if (viewModel.storeRequest.categories!!.contains(item.id!!.toInt())) {
                                                storeCategoryNames.add(item.name.toString())
                                            }
                                        }
                                        binding.etStoreType.text =
                                            storeCategoryNames.joinToString { it }
                                        // viewModel.storeRequest.tags = null
                                        // binding.etStoreTags.text = ""
                                        if (viewModel.storeRequest.categories != null && viewModel.storeRequest.categories!!.size > 0) {
                                            viewModel.getTags()
                                            viewModel.storeRequest.tags = storeData.tags
                                            for (item in viewModel.tagsList) {
                                                if (viewModel.storeRequest.tags?.contains(item.id) == true) {
                                                    storeTagsNames.add(item.name.toString())
                                                }
                                            }
                                            binding.etStoreTags.text =
                                                storeTagsNames.joinToString { it }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Codes.STORE_TAGS_SUCSSES -> {
                        when {
                            intent.hasExtra(Params.STORE_REGISTER) -> {
                                when {
                                    intent.getSerializableExtra(Params.STORE_REGISTER) != null -> {
                                        val storeData: StoreRegister =
                                            intent.extras!!.getSerializable(Params.STORE_REGISTER) as StoreRegister

                                        viewModel.storeRequest.tags = storeData.tags
                                        for (item in viewModel.tagsList) {
                                            if (viewModel.storeRequest.tags?.contains(item.id) == true) {
                                                storeTagsNames.add(item.name.toString())
                                            }
                                        }
                                        binding.etStoreTags.text =
                                            storeTagsNames.joinToString { it }
                                    }
                                }
                            }
                        }
                    }

                    Codes.STORE_CATEGORIES_CLICKED -> {
                        val bundle = Bundle()
                        bundle.putSerializable(Params.STORE_CATEGORIES, viewModel.typesResponse)
                        if (viewModel.storeRequest.categories != null) {
                            bundle.putSerializable(
                                Params.STORE_CATEGORIES_DATA,
                                viewModel.storeRequest
                            )
                        }
                        Utils.startDialogActivity(
                            this,
                            DialogStoreTypesFragment::class.java.name,
                            Codes.DIALOG_STORE_CATEGORY_CODE,
                            bundle
                        )
                    }

                    Codes.STORE_TAGS_CLICKED -> {
                        val bundle = Bundle()
                        bundle.putSerializable(Params.STORE_TAGS, viewModel.tagsResponse)
                        if (viewModel.storeRequest.tags != null) {
                            bundle.putSerializable(Params.STORE_TAGS_DATA, viewModel.storeRequest)
                        }
                        Utils.startDialogActivity(
                            this,
                            DialogStoreTagsFragment::class.java.name,
                            Codes.DIALOG_STORE_TAGS_CODE,
                            bundle
                        )
                    }

                    Codes.SELECT_STORE_IMAGE -> {
                        pickImage(Codes.STORE_IMAGE, Options.Mode.Picture)
                    }

                    Codes.SELECT_STORE_THUMB -> {
                        pickImage(Codes.STORE_THUMBNAIL, Options.Mode.Picture)
                    }

                    Codes.LOCATION_CLICKED -> {
                        val intent = Intent(this, MapsActivity::class.java)
                        intent.putExtra("from", Codes.GETTING_USER_LOCATION)
                        startActivityForResult(intent, Codes.GETTING_USER_LOCATION)
                    }

                    Codes.SHOW_CONDITIONS -> {
                        startActivity(Intent(this, ConditionActivity::class.java))
                    }

                    Codes.EMPTY_TYPE -> {
                        showToast(getString(R.string.choose_store_type), 1)
                    }

                    Codes.EMPTY_TAGS -> {
                        showToast(getString(R.string.choose_the_section_type), 1)
                    }

                    Codes.EMPTY_STORE_NAME -> {
                        showToast(getString(R.string.enter_your_store_name), 1)
                    }

                    Codes.EMPTY_STORE_LAT -> {
                        showToast(getString(R.string.please_enter_store_direction), 1)
                    }

                    Codes.EMPTY_STORE_ADDRESS -> {
                        showToast(getString(R.string.please_enter_store_address), 1)
                    }

                    Codes.EMPTY_STORE_MINIMUM -> {
                        showToast(getString(R.string.enter_the_minimum_order), 1)
                    }

                    Codes.EMPTY_STORE_PHONE -> {
                        showToast(getString(R.string.enter_phone_number), 1)
                    }

                    Codes.INVALID_PHONE -> {
                        showToast(getString(R.string.invalid_phone), 1)
                    }

                    Codes.EMPTY_STORE_WHATS -> {
                        showToast(getString(R.string.enter_the_whatsapp_number), 1)
                    }

                    Codes.INVALID_STORE_WHATS -> {
                        showToast(getString(R.string.invalid_whatsapp), 1)
                    }

                    Codes.EMPTY_STORE_PRICE -> {
                        showToast(getString(R.string.enter_the_delivery_price), 1)
                    }

                    Codes.EMPTY_STORE_Space -> {
                        showToast(
                            getString(R.string.enter_the_space_available_for_delivery_to_it),
                            1
                        )
                    }

                    Codes.EMPTY_STORE_TIME -> {
                        showToast(getString(R.string.enter_the_average_delivery_time), 1)
                    }

                    Codes.EMPTY_STORE_STORE_IMG -> {
                        showToast(getString(R.string.please_enter_store_image), 1)
                    }

                    Codes.EMPTY_STORE_STORE_THUMB -> {
                        showToast(getString(R.string.please_enter_store_thumbnail), 1)
                    }

                    Codes.FALSE_CHECK_CONDITION -> {
                        showToast(getString(R.string.agree_terms), 1)
                    }

                    Codes.SUCCESS -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                        intent.putExtra(Params.STORE_REGISTER, viewModel.storeRequest)
                        setResult(Codes.REGISTER_STOREINFO_REQUEST_CODE, intent)
                        finish()
                    }

                    Codes.BACK_PRESSED -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                        setResult(Codes.REGISTER_STOREINFO_REQUEST_CODE, intent)
                        finish()
                    }

                    is String -> {
                        showToast(it.toString(), 1)
                    }
                }
            }
        }
    }

    fun showToast(msg: String, type: Int) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Codes.STORE_IMAGE -> {
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

                        binding.notStoreImg.visibility = View.GONE
                        binding.ivStorePhoto.visibility = View.VISIBLE
                        binding.ivStoreImg.setImageURI(fileUri)
                        viewModel.storeRequest.storeThumb = file
                        viewModel.storeRequest.storeThumbUri = fileUri.toString()
                    }

                    Codes.STORE_THUMBNAIL -> {
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
                        binding.notThumbImg.visibility = View.GONE
                        binding.ivThumb.visibility = View.VISIBLE
                        binding.ivStoreThumb.setImageURI(fileUri)
                        viewModel.storeRequest.storeImg = file
                        viewModel.storeRequest.storeImgUri = fileUri.toString()
                    }


                }
            }
        }

        /* When user select his location manually from map activity*/
        when {
            requestCode == Codes.GETTING_USER_LOCATION && data != null -> {
                when {
                    data.hasExtra(Params.USER_LOCATION) -> {
                        val locationItem =
                            data.getParcelableExtra<UserLocation>(Params.USER_LOCATION)
                        when {
                            locationItem != null -> {
                                viewModel.storeRequest.storeLat = locationItem.lat!!.toString()
                                viewModel.storeRequest.storeLng = locationItem.lng!!.toString()
                                viewModel.storeRequest.storeAddress =
                                    locationItem.address.toString()
                                binding.etAddress.setText(locationItem.address.toString())
                            }
                        }
                    }
                }
            }

            requestCode == Codes.DIALOG_STORE_CATEGORY_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            binding.etStoreType.text = data.getStringExtra("storeCategoryNames")
                            viewModel.storeRequest.categories =
                                data.getIntegerArrayListExtra("storeCategoryId")
                            viewModel.getTags()
                        }
                    }
                }
            }

            requestCode == Codes.DIALOG_STORE_TAGS_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            binding.etStoreTags.text = data.getStringExtra("storeTagNames")
                            viewModel.storeRequest.tags =
                                data.getIntegerArrayListExtra("storeTagId")
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
}