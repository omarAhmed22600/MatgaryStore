package com.brandsin.store.ui.dialogs.toast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.brandsin.store.R
import com.brandsin.store.databinding.DialogToastBinding
import com.brandsin.store.model.constants.Params
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogToastFragment : DialogFragment()
{
    private lateinit var binding: DialogToastBinding
    var message = ""
    var toastType = 1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(Params.DIALOG_TOAST_MESSAGE)) {
                message = requireArguments().getString(Params.DIALOG_TOAST_MESSAGE, "")
                toastType = requireArguments().getInt(Params.DIALOG_TOAST_TYPE, 1)
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DialogToastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMessage.text = message

        /*
        * toastType == 1 >> failed toast
        * toastType == 2 >> success toast
        */
        if (toastType == 1) {
            binding.ivToast.setImageResource(R.drawable.ic_toast_failed)
            binding.toastLayout.background = ContextCompat.getDrawable(requireActivity(), R.drawable.toast_failed_bg)
        } else {
            binding.ivToast.setImageResource(R.drawable.ic_toast_success)
            binding.toastLayout.background = ContextCompat.getDrawable(requireActivity(), R.drawable.toast_success_bg)
        }

        lifecycleScope.launch {
            delay(2000)
            requireActivity().finish()
        }
    }
}
