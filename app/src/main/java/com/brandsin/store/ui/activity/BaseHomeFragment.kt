package com.brandsin.store.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.brandsin.store.ui.activity.home.HomeActivity

open class BaseHomeFragment : BaseFragment() {

    /*override fun onDestroyView() {
        super.onDestroyView()
        if (!isNavigated)
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                val navController = findNavController()
                if (navController.currentBackStackEntry?.destination?.id != null) {
                    findNavController().navigateUp()
                } else
                    navController.popBackStack()
            }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setBarName(title: String) {
        (requireActivity() as HomeActivity).run {
            viewModel?.obsTitle?.set(title)
        }
    }
}

