package com.brandsin.store.ui.dialogs.confirm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brandsin.store.databinding.DialogConfirmBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.offers.listoffer.OffersItemDetails
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.model.constants.Params

class DialogConfirmFragment  : DialogFragment()
{
    lateinit  var  binding: DialogConfirmBinding
    var message : String = ""
    var positiveBtn : String = ""
    var negativeBtn : String = ""
    var offerItem = OffersItemDetails()
    var productItem = ProductsItem()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        {
            if (requireArguments().containsKey(Params.DIALOG_CONFIRM_MESSAGE))
            {
                message = requireArguments().getString(Params.DIALOG_CONFIRM_MESSAGE, null)
                positiveBtn = requireArguments().getString(Params.DIALOG_CONFIRM_POSITIVE, null)
                negativeBtn = requireArguments().getString(Params.DIALOG_CONFIRM_NEGATIVE, null)

                if (requireArguments().containsKey(Params.OFFER_ITEM))
                {
                    offerItem = requireArguments().getSerializable(Params.OFFER_ITEM) as OffersItemDetails
                }

                if (requireArguments().containsKey(Params.PRODUCT_ITEM))
                {
                    productItem = requireArguments().getSerializable(Params.PRODUCT_ITEM) as ProductsItem
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DialogConfirmBinding.inflate(inflater, container, false)

        binding.tvMessage.text = message
        binding.btnConfirm.text = positiveBtn
        binding.btnIgnore.text = negativeBtn

        binding.btnIgnore.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
            requireActivity().setResult(Codes.DIALOG_CONFIRM_REQUEST, intent)
            requireActivity().finish()
        }

        binding.btnConfirm.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)

            if (offerItem.id != null) {
                intent.putExtra(Params.OFFER_ITEM, offerItem.id)
            }

            if (productItem.id != null) {
                intent.putExtra(Params.PRODUCT_ITEM, productItem.id)
            }

            requireActivity().setResult(Codes.DIALOG_CONFIRM_REQUEST, intent)
            requireActivity().finish()
        }
        return binding.root
    }
}