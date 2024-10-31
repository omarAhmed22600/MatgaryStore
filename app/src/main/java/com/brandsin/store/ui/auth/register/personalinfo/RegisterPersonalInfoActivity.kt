package com.brandsin.store.ui.auth.register.personalinfo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityRegisterPersonalInfoBinding
import com.brandsin.store.model.auth.register.UserRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
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

class RegisterPersonalInfoActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var binding: ActivityRegisterPersonalInfoBinding

    lateinit var viewModel: RegisterPersonalInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_personal_info)

        viewModel = ViewModelProvider(this)[RegisterPersonalInfoViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

        when {
            intent.hasExtra(Params.USER_REGISTER) -> {
                when {
                    intent.getSerializableExtra(Params.USER_REGISTER) != null -> {
                        val userData: UserRegister =
                            intent.extras!!.getSerializable(Params.USER_REGISTER) as UserRegister
                        viewModel.userData = userData

                        binding.notOfferImg.visibility = View.GONE
                        binding.ivPhoto.visibility = View.VISIBLE
                        Glide.with(this).load(userData.userIdImgUri).into(binding.ivUserImg)
                        viewModel.userData.userIdImg = userData.userIdImg
                        viewModel.userData.userIdImgUri = userData.userIdImgUri
                    }
                }
            }
        }
    }

    override fun onChanged(value: Any?) {
        when (value) {
            null -> return
            else -> value.let {
                when (it) {
                    Codes.SELECT_IMAGES -> {
                        pickImage(Codes.PERSONAL_IMG, Options.Mode.Picture)
                    }

                    Codes.NAME_EMPTY -> {
                        showToast(getString(R.string.please_enter_your_first_name), 1)
                    }

                    Codes.EMPTY_LAST_NAME -> {
                        showToast(getString(R.string.please_enter_your_last_name), 1)
                    }

                    Codes.EMPTY_PHONE -> {
                        showToast(getString(R.string.please_enter_your_phone), 1)
                    }

                    Codes.INVALID_PHONE -> {
                        showToast(getString(R.string.invalid_phone), 1)
                    }

                    Codes.EMAIL_EMPTY -> {
                        showToast(getString(R.string.please_enter_your_email), 1)
                    }

                    Codes.EMAIL_INVALID -> {
                        showToast(getString(R.string.email_must_correct), 1)
                    }

                    Codes.EMPTY_IMAGE -> {
                        showToast(getString(R.string.please_enter_your_image), 1)
                    }

                    Codes.SUCCESS -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                        intent.putExtra(Params.USER_REGISTER, viewModel.userData)
                        setResult(Codes.REGISTER_PERSONALINFO_REQUEST_CODE, intent)
                        finish()
                        Timber.e("Mou3aaaaaz : " + viewModel.userData.toString())
                    }

                    Codes.BACK_PRESSED -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                        setResult(Codes.REGISTER_PERSONALINFO_REQUEST_CODE, intent)
                        finish()
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
                    Codes.PERSONAL_IMG -> {
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
                        binding.ivUserImg.setImageURI(fileUri)
                        viewModel.userData.userIdImg = file
                        viewModel.userData.userIdImgUri = fileUri.toString()
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