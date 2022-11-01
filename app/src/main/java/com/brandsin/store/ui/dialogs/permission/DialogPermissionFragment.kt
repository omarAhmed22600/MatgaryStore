package com.brandsin.store.ui.dialogs.permission

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brandsin.store.databinding.DialogPermissionBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.user.model.constants.Params

class DialogPermissionFragment  : DialogFragment()
{
    lateinit  var  binding: DialogPermissionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DialogPermissionBinding.inflate(inflater, container, false)

        binding.btnIgnore.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
            requireActivity().setResult(Codes.DIALOG_CONFIRM_REQUEST, intent)
            requireActivity().finish()
        }

        binding.btnConfirm.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
            requireActivity().setResult(Codes.DIALOG_CONFIRM_REQUEST, intent)
            requireActivity().finish()
        }
        return binding.root
    }
}