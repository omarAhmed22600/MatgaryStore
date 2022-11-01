package com.brandsin.store.ui.dialogs.addoffer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brandsin.store.databinding.DialogAddOfferBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.user.model.constants.Params

class DialogAddOfferFragment  : DialogFragment()
{
    lateinit  var  binding: DialogAddOfferBinding
    var message : String = ""
    var requestCode : Int = -1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        when {
            arguments != null -> {
                when {
                    requireArguments().containsKey(Params.DIALOG_CONFIRM_MESSAGE) -> {
                        message = requireArguments().getString(Params.DIALOG_CONFIRM_MESSAGE, null)
                        requestCode = requireArguments().getInt(Params.REQUEST_CODE, 0)
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DialogAddOfferBinding.inflate(inflater, container, false)

        binding.tvMessage.text = message

        binding.btnOffers.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
            intent.putExtra(Params.REQUEST_CODE, requestCode)
            requireActivity().setResult(Codes.DIALOG_OFFER_REQUEST, intent)
            requireActivity().finish()
        }

        binding.btnOrders.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Params.DIALOG_CLICK_ACTION, 2)
            intent.putExtra(Params.REQUEST_CODE, requestCode)
            requireActivity().setResult(Codes.DIALOG_OFFER_REQUEST, intent)
            requireActivity().finish()
        }
        return binding.root
    }
}