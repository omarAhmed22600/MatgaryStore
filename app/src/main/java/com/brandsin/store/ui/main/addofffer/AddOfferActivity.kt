package com.brandsin.store.ui.main.addofffer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityAddOfferBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.offers.add.CreateOfferResponse
import com.brandsin.store.model.main.offers.add.DataItem
import com.brandsin.store.model.main.offers.add.OfferAddProductResponse
import com.brandsin.store.model.main.offers.listoffer.OffersItemDetails
import com.brandsin.store.model.main.offers.update.UpdateOfferResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.offertime.DialogOfferTimeFragment
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.observe
import com.brandsin.store.utils.visible
import com.brandsin.store.model.constants.Params
import com.brandsin.store.ui.activity.BaseFragment
import com.brandsin.store.utils.map.PermissionUtil
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddOfferActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var binding: ActivityAddOfferBinding

    lateinit var viewModel: AddOfferViewModel

    private var productsNamesList = ArrayList<String>()

    var onceFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            tempFileUri = savedInstanceState.getParcelable("photoURI")
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_offer)

        viewModel = ViewModelProvider(this)[AddOfferViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

        initView()

        when {
            intent.hasExtra(Params.OFFER_ITEM) -> {
                when {
                    intent.getSerializableExtra(Params.OFFER_ITEM) != null -> {
                        val offerData: OffersItemDetails =
                            intent.extras!!.getSerializable(Params.OFFER_ITEM) as OffersItemDetails

                        viewModel.setOfferData(2, offerData)

                        binding.notOfferImg.visibility = View.GONE
                        binding.ivPhoto.visibility = View.VISIBLE


                        if (offerData.image != null) {
                            Glide.with(this)
                                .load(offerData.image)
                                .error(R.drawable.app_logo)
                                .into(binding.ivOfferImg)
                            onceFlag = true
                        } else {
                            binding.notOfferImg.visibility = View.VISIBLE
                            binding.ivPhoto.visibility = View.GONE
                        }
                        viewModel.obsToolBarTitle.set(getString(R.string.update_offer))
                        Timber.e("${offerData.type}")
                        if (offerData.type == "product") {
                            binding.radioBtnProduct.isChecked = true
                            binding.radioBtnGift.isChecked = false
                        } else {
                            binding.radioBtnGift.isChecked = true
                            binding.radioBtnProduct.isChecked = false
                            binding.tvAutoComplete.gone()
                            binding.seperator.gone()
                        }

                        viewModel.offerType = offerData.type
                        viewModel.updateOfferRequest.type = offerData.type
                    }

                    else -> {
                        viewModel.setOfferData(1)
                        binding.notOfferImg.visibility = View.VISIBLE
                        binding.ivPhoto.visibility = View.GONE
                        viewModel.obsToolBarTitle.set(getString(R.string.add_offer))

                        binding.radioBtnProduct.isChecked = true
                        binding.radioBtnGift.isChecked = false
                    }
                }
            }

            else -> {
                viewModel.setOfferData(1)
                binding.notOfferImg.visibility = View.VISIBLE
                binding.ivPhoto.visibility = View.GONE
                viewModel.obsToolBarTitle.set(getString(R.string.add_offer))

                binding.radioBtnProduct.isChecked = true
                binding.radioBtnGift.isChecked = false
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
                        is OfferAddProductResponse -> {
                            if (it.data.data?.isNotEmpty() == true) {
                                setAutocomplete(it.data.data)
                            } else {
                                // showToast(getString(R.string.no_results_search) , 1)
                            }
                        }

                        is CreateOfferResponse -> {
                            showToast(it.data.message.toString(), 2)
                            finish()
                            /*val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.ADD_OFFER)
                            Utils.startDialogActivity(
                                this,
                                DialogAddOfferFragment::class.java.name,
                                Codes.DIALOG_OFFER_REQUEST,
                                bundle
                            )*/
                        }

                        is UpdateOfferResponse -> {
                            showToast(it.data.message.toString(), 2)
                            finish()
                            /*val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.UPDATE_OFFER)
                            Utils.startDialogActivity(
                                this,
                                DialogAddOfferFragment::class.java.name,
                                Codes.DIALOG_OFFER_REQUEST,
                                bundle
                            )*/
                        }
                    }
                }

                else -> {
                    Timber.e(it.message)
                }
            }
        }

        viewModel.productsAdapter.deleteLiveData.observe(this) {
            viewModel.addOfferRequest.productsIds?.remove(it?.id!!.toInt())
            viewModel.productsList.remove(it)
            viewModel.productsAdapter.updateList(viewModel.productsList)
        }

        viewModel.productsUpdateAdapter.deleteLiveData.observe(this) {
            viewModel.prevOfferProducts.remove(it)
            viewModel.productsUpdateAdapter.updateList(viewModel.prevOfferProducts)
        }

        binding.tvAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val length: Int = binding.tvAutoComplete.length()
                viewModel.offerAddProductRequest.name = binding.tvAutoComplete.text.toString()
                if (length > 0) {
                    if (!isFinishing) {
                        viewModel.getStoreProducts()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        setBtnListener()
    }

    private fun initView() {
        binding.radioBtnProduct.isChecked = true
    }

    private fun setBtnListener() {
        binding.ibBack.setOnClickListener {
            finish()
        }

        binding.radioGroupOfferType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioBtnProduct -> {
                    viewModel.offerType = "product"
                    binding.tvAutoComplete.visible()
                    binding.seperator.visible()
                    binding.rvProducts.visible()
                    binding.rvProductsUpdate.visible()
                }

                R.id.radioBtnGift -> {
                    viewModel.offerType = "gift"
                    binding.tvAutoComplete.gone()
                    binding.seperator.gone()
                    binding.rvProducts.gone()
                    binding.rvProductsUpdate.gone()
                }
            }
        }
    }

    private fun setAutocomplete(data: List<DataItem?>) {
        productsNamesList = ArrayList()

        data.forEach { productItem ->
            if (productItem != null) {
                productsNamesList.add(productItem.name.toString())
            }
        }
        val adapter = ArrayAdapter(
            MyApp.getInstance(),
            R.layout.raw_offer_auto_complete,
            productsNamesList
        )
        binding.tvAutoComplete.threshold = 1
        binding.tvAutoComplete.setAdapter(adapter)

        binding.tvAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ -> // parent, view, position, id
                data.forEach { productItem ->
                    if (productItem != null) {
                        if (productItem.name == binding.tvAutoComplete.text.toString()) {
                            if (viewModel.addOfferRequest.productsIds == null) {
                                viewModel.addOfferRequest.productsIds = ArrayList()
                            }
                            if (!viewModel.addOfferRequest.productsIds!!.contains(productItem.id!!.toInt())) {
                                viewModel.addOfferRequest.productsIds!!.add(productItem.id.toInt())
                                viewModel.productsList.add(productItem)
                                viewModel.productsAdapter.updateList(viewModel.productsList)
                            } else {
                                showToast(getString(R.string.product_added_before), 1)
                            }
                        }
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

    override fun onChanged(value: Any?) {
        when (value) {
            null -> return
            else -> value.let {
                when (it) {
                    Codes.SELECT_IMAGES -> {
                        pickImage(Codes.OFFER_IMG_REQUEST_CODE, Options.Mode.All)
                    }

                    Codes.CHANGE_IMAGES -> {
                        pickImage(Codes.OFFER_IMG_UPDATE_REQUEST_CODE, Options.Mode.All)
                    }

                    Codes.EMPTY_IMAGE -> {
                        showToast(getString(R.string.please_select_offer_image), 1)
                    }

                    Codes.EMPTY_OFFER_NAME -> {
                        showToast(getString(R.string.please_select_offer_name_ar), 1)
                    }

                    Codes.EMPTY_OFFER_NAME_EN -> {
                        showToast(getString(R.string.please_select_offer_name_en), 1)
                    }

                    Codes.EMPTY_OFFER_DESCRIPTION -> {
                        showToast(getString(R.string.please_select_offer_description_ar), 1)
                    }

                    Codes.EMPTY_OFFER_DESCRIPTION_EN -> {
                        showToast(getString(R.string.please_select_offer_description_en), 1)
                    }

                    Codes.EMPTY_OFFER_PRICE -> {
                        showToast(getString(R.string.please_select_offer_price), 1)
                    }

                    Codes.OFFER_PRICE_LESS_OFFER_PRICE_TO -> {
                        showToast(getString(R.string.please_select_offer_price), 1)
                    }

                    Codes.SELECT_START_DATE -> {
                        showToast(getString(R.string.select_start_date), 1)
                    }

                    Codes.SELECT_END_DATE -> {
                        showToast(getString(R.string.select_end_date), 1)
                    }

                    Codes.EMPTY_PRODUCTS -> {
                        showToast(getString(R.string.please_select_offer_product), 1)
                    }

                    Codes.START_AFTER_END -> {
                        showToast(getString(R.string.start_after_end), 1)
                    }

                    Codes.START_EQUAL_END -> {
                        showToast(getString(R.string.start_equal_end), 1)
                    }

                    Codes.SHOW_START_DATE -> {
                        val bundle = Bundle()
                        bundle.putString("START_DATE", viewModel.obsStartDate.get())
                        bundle.putInt(Params.DIALOG_DATE_REQUEST, Codes.SHOW_START_DATE)
                        Utils.startDialogActivity(
                            this,
                            DialogOfferTimeFragment::class.java.name,
                            Codes.DIALOG_OFFER_TIME,
                            bundle
                        )
                    }

                    Codes.SHOW_END_DATE -> {
                        val bundle = Bundle()
                        bundle.putString("END_DATE", viewModel.obsEndDate.get())
                        bundle.putInt(Params.DIALOG_DATE_REQUEST, Codes.SHOW_END_DATE)
                        Utils.startDialogActivity(
                            this,
                            DialogOfferTimeFragment::class.java.name,
                            Codes.DIALOG_OFFER_TIME,
                            bundle
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("$requestCode $resultCode $data")
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Codes.OFFER_IMG_REQUEST_CODE -> {
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
                        binding.notOfferImg.visibility = View.GONE
                        binding.ivPhoto.visibility = View.VISIBLE

                        viewModel.isImageChanged = true

                        Timber.e("uri $fileUri")
                        if (/*fileUri.toString().toLowerCase(Locale.ROOT).contains("img")*/
                            file.extension.toLowerCase(Locale.ROOT).contains("jpg")
                            || file.extension.toLowerCase(Locale.ROOT).contains("png")
                            || file.extension.toLowerCase(Locale.ROOT).contains("jpeg")
                        ) {
                            // Handle image selection
                            Timber.e("image")
                            viewModel.isImage = true
                            binding.ivOfferImg.setImageURI(fileUri)
                            binding.ivVideo.visibility = View.GONE
                            viewModel.updateOfferRequest.offerImage = file
                            viewModel.updateOfferRequest.offerVideo = null
                            viewModel.addOfferRequest.offerImage = file
                            viewModel.addOfferRequest.offerVideo = null
                        } else if (/*fileUri.toString().toLowerCase(Locale.ROOT).contains("vid")
                                    ||*/ file.extension.toLowerCase(Locale.ROOT).contains("mp4")
                            || file.extension.toLowerCase(Locale.ROOT).contains("mkv")
                            || file.extension.toLowerCase(Locale.ROOT).contains("mpeg")
                            || file.extension.toLowerCase(Locale.ROOT).contains("wmv")
                            || file.extension.toLowerCase(Locale.ROOT).contains("amv")
                            || file.extension.toLowerCase(Locale.ROOT).contains("mov")
                        ) {
                            // Handle video selection
                            Timber.e("video")

                            val videoFile = file
                            viewModel.isImage = false
                            val retriever = MediaMetadataRetriever()
                            retriever.setDataSource(videoFile.path)
                            val thumbnail = retriever.getFrameAtTime(
                                1000000,
                                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                            ) // Get frame at 1 second (in microseconds)
                            retriever.release()
                            binding.ivOfferImg.setImageBitmap(thumbnail)
                            viewModel.addOfferRequest.offerVideo = videoFile
                            viewModel.addOfferRequest.offerImage = null
                            viewModel.updateOfferRequest.offerVideo = videoFile
                            viewModel.updateOfferRequest.offerImage = null
                            binding.ivVideo.visibility = View.VISIBLE
                            // You can also use a video thumbnail generator here if needed
                        } else {
                            Timber.e("Unknown File")
                        }


                    }

                    Codes.OFFER_IMG_UPDATE_REQUEST_CODE -> {
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
                        binding.notOfferImg.visibility = View.GONE
                        binding.ivPhoto.visibility = View.VISIBLE

                        val fileType =
                            contentResolver.getType(fileUri) // Get MIME type of the file
                        viewModel.isImageChanged = true
                        if (/*fileUri.toString().toLowerCase(Locale.ROOT).contains("img")*/
                            file.extension.toLowerCase(Locale.ROOT).contains("jpg")
                            || file.extension.toLowerCase(Locale.ROOT).contains("png")
                            || file.extension.toLowerCase(Locale.ROOT).contains("jpeg")
                        ) {
                            // Handle image selection
                            Timber.e("image ${fileUri.toString()}")
                            viewModel.isImage = true
                            binding.ivOfferImg.setImageURI(fileUri)
                            binding.ivVideo.visibility = View.GONE
                            viewModel.updateOfferRequest.offerImage = file
                            viewModel.updateOfferRequest.offerVideo = null
                            viewModel.addOfferRequest.offerImage = file
                            viewModel.addOfferRequest.offerVideo = null
                        } else if (/*fileUri.toString().toLowerCase(Locale.ROOT).contains("vid")
                                    ||*/ file.extension.toLowerCase(Locale.ROOT).contains("mp4")
                            || file.extension.toLowerCase(Locale.ROOT).contains("mkv")
                            || file.extension.toLowerCase(Locale.ROOT).contains("mpeg")
                            || file.extension.toLowerCase(Locale.ROOT).contains("wmv")
                            || file.extension.toLowerCase(Locale.ROOT).contains("amv")
                            || file.extension.toLowerCase(Locale.ROOT).contains("mov")
                        ) {
                            // Handle video selection
                            Timber.e("video")
                            val videoFile = file
                            viewModel.isImage = false
                            val retriever = MediaMetadataRetriever()
                            retriever.setDataSource(videoFile.path)
                            val thumbnail = retriever.getFrameAtTime(
                                1000000,
                                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                            ) // Get frame at 1 second (in microseconds)
                            retriever.release()
                            binding.ivOfferImg.setImageBitmap(thumbnail)
                            viewModel.updateOfferRequest.offerVideo = videoFile
                            viewModel.updateOfferRequest.offerImage = null
                            viewModel.addOfferRequest.offerVideo = videoFile
                            viewModel.addOfferRequest.offerImage = null
                            binding.ivVideo.visibility = View.VISIBLE
                            // You can also use a video thumbnail generator here if needed
                        } else {
                            Timber.e("Unknown File")
                        }
                    }
                }


            }
        }

        when {
            requestCode == Codes.DIALOG_OFFER_TIME && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                                when {
                                    data.getIntExtra(Params.DIALOG_DATE_REQUEST, 0) != 0 -> {
                                        when (Codes.SHOW_START_DATE) {
                                            data.getIntExtra(Params.DIALOG_DATE_REQUEST, 0) -> {
                                                viewModel.obsStartDate.set(
                                                    data.getStringExtra(Params.OFFER_TIME)
                                                )
                                                if (!viewModel.obsEndDate.get().isNullOrEmpty()) {
                                                    viewModel.checkValidationDates()
                                                }
                                            }
                                        }
                                        when (Codes.SHOW_END_DATE) {
                                            data.getIntExtra(Params.DIALOG_DATE_REQUEST, 0) -> {
                                                viewModel.obsEndDate.set(data.getStringExtra(Params.OFFER_TIME))
                                                viewModel.checkValidationDates()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        when {
            requestCode == Codes.DIALOG_OFFER_REQUEST && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                                if (data.getIntExtra(Params.REQUEST_CODE, 0) == Codes.ADD_OFFER) {
                                    val intent = Intent()
                                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                                    setResult(Codes.ADD_OFFER, intent)
                                    finish()
                                }
                                if (data.getIntExtra(Params.REQUEST_CODE, 0)
                                    == Codes.UPDATE_OFFER
                                ) {
                                    val intent = Intent()
                                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                                    setResult(Codes.UPDATE_OFFER, intent)
                                    finish()
                                }
                            }

                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 2 -> {
                                if (data.getIntExtra(Params.REQUEST_CODE, 0) == Codes.ADD_OFFER) {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finishAffinity()
                                }
                                if (data.getIntExtra(Params.REQUEST_CODE, 0)
                                    == Codes.UPDATE_OFFER
                                ) {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finishAffinity()
                                }
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
}