package com.brandsin.store.ui.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
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
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.model.constants.Params
import com.fxn.pix.Options

import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class BaseFragment : Fragment() {

    var isNavigated = false

    val storagePermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    private val permissionsResults = MutableLiveData<Map<String, Boolean>?>()
    val permissionsResultsLive = permissionsResults

    private val activityResultsData = MutableLiveData<Intent?>()
    val activityResultsDataLive = activityResultsData

    private var requestPermissionsCallBack = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        permissionsResults.value = it
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                activityResultsData.value = data
            }
        }

    protected fun launchPermission(permissions: Array<String>) {
        try {
            requestPermissionsCallBack.launch(permissions)
        } catch (ex: ActivityNotFoundException) {

        }
    }

    protected fun launchActivityForResult(intent: Intent) {
        try {
            resultLauncher.launch(intent)
        } catch (ex: Exception) {
            Log.d("myDebug", "BaseFragment launchActivityForResult   " + ex.localizedMessage)
        }
    }

    protected fun isPermissionGranted(permission: String): Boolean {
        return try {
            ContextCompat.checkSelfPermission(
                requireContext(), permission
            ) == PackageManager.PERMISSION_GRANTED
        } catch (ex: IllegalStateException) {
            return false
        }
    }

    fun navigateWithAction(action: NavDirections) {
        isNavigated = true
        findNavController().navigate(action)
    }

    fun navigate(resId: Int) {
        isNavigated = true
        findNavController().navigate(resId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!isNavigated)
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                val navController = findNavController()
                if (navController.currentBackStackEntry?.destination?.id != null) {
                    findNavController().navigateUp()
                } else
                    navController.popBackStack()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun showToast(msg: String, type: Int) {
        // Success 2
        // False  1
        val bundle = Bundle()
        bundle.putString(Params.DIALOG_TOAST_MESSAGE, msg)
        bundle.putInt(Params.DIALOG_TOAST_TYPE, type)
        Utils.startDialogActivity(
            requireActivity(),
            DialogToastFragment::class.java.name,
            Codes.DIALOG_TOAST_REQUEST,
            bundle
        )
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
                    showToast(getString(R.string.someThing_went_wrong),1)
                }
            }
        }else {
            Timber.e("not all permissions granted")
            // Handle permission request result in the onRequestPermissionsResult
            // This is handled in the checkAndRequestPermissions method
            checkAndRequestAllPermissions()
        }
    }
    private fun showDialogToChooseMediaType(requestCode: Int) {
        val options = arrayOf(getString(R.string.photo), getString(R.string.video))

        AlertDialog.Builder(requireContext())
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

        AlertDialog.Builder(requireContext())
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

        AlertDialog.Builder(requireContext())
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            tempFileUri = savedInstanceState.getParcelable("photoURI")
        }
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
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    // Helper function to create video file
    @Throws(IOException::class)
    private fun createVideoFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES)
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
            tempFileUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
            startActivityForResult(intent, requestCode) // Use the requestCode passed to this function
        }
    }
    private fun openCameraForVideo(requestCode: Int) {
        // Implement logic to open camera for capturing a video
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val videoFile = createVideoFile() // Create a file to save the video
        videoFile?.also {
            tempFileUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", it)
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
            requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun hasPermission(permissions: Array<String>): Boolean {
        return permissions.all { ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (hasPermission(permissionsToRequest.toTypedArray())) {
                // Permission granted, continue with your task
                showToast(getString(R.string.permission_granted_try_again),2)
            } else {
                // Permission denied
                // Check if the user has denied the permission without checking "Don't ask again"
                val shouldShowRationale = permissions.any {
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it)
                }

                if (shouldShowRationale) {
                    // Show a rationale dialog or Toast message explaining why the permission is needed
                    showToast(getString(R.string.permissions_are_needed_to_access_your_media), 1)

                    // Request permissions again
                    requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
                } else {
                    // The user has selected "Don't ask again". You might want to disable the feature or show a message.
                    showToast(getString(R.string.permissions_denied_please_enable_them_in_settings), 1)
                }
            }
        }
    }

    fun showExplanation(
        title: String, message: String, permissions: Array<String>,
    ) {
        val builder = AlertDialog.Builder(
            requireContext(),
        )
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, id ->
                dialog.dismiss()
                val intent =  Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + requireContext().packageName)
                startActivity(intent)
            }
        builder.create().show()
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1001
    }
}

