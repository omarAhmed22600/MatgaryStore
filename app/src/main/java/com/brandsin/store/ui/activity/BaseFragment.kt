package com.brandsin.store.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.user.model.constants.Params
import com.brandsin.user.utils.map.PermissionUtil

open class BaseFragment : Fragment()
{
    var isNavigated = false

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
                if (navController.currentBackStackEntry?.destination?.id != null)
                {
                    findNavController().navigateUp()
                }
                else
                    navController.popBackStack()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }

    fun showToast(msg : String, type : Int) {
        //succss 2
        //false  1
        val bundle = Bundle()
        bundle.putString(Params.DIALOG_TOAST_MESSAGE, msg)
        bundle.putInt(Params.DIALOG_TOAST_TYPE, type)
        Utils.startDialogActivity(requireActivity(), DialogToastFragment::class.java.name, Codes.DIALOG_TOAST_REQUEST, bundle)
    }

    fun pickImage(requestCode: Int) {
        val options = Options.init()
            .setRequestCode(requestCode) //Request code for activity results
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setMode(Options.Mode.All)
        if (PermissionUtil.hasImagePermission(requireActivity())) {
            Pix.start(this, options)
        } else {
            observe(PermissionUtil.requestPermission(
                requireActivity(),
                PermissionUtil.getImagePermissions()
            )
            ) {
                when (it) {
                    PermissionUtil.AppPermissionResult.AllGood -> Pix.start(
                        this,
                        options
                    )
                }
            }
        }
    }
}

