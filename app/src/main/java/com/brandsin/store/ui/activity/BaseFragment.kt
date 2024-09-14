package com.brandsin.store.ui.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import com.brandsin.user.utils.map.PermissionUtil
import com.fxn.pix.Options
import com.fxn.pix.Pix

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

    fun pickImage(requestCode: Int, mode: Options.Mode? = null) {
        val options = Options.init()
            .setRequestCode(requestCode) //Request code for activity results
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setMode(mode)
        if (PermissionUtil.hasImagePermission(requireActivity())) {
            Pix.start(this, options)
        } else {
            observe(
                PermissionUtil.requestPermission(
                    requireActivity(),
                    PermissionUtil.getImagePermissions()
                )
            ) {
                when (it) {
                    PermissionUtil.AppPermissionResult.AllGood -> Pix.start(
                        this,
                        options
                    )

                    else -> {}
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
}

