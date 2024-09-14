package com.brandsin.store.ui.main.refundableProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentRejectRefundableBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.ui.main.refundableProduct.viewmodel.RefundableProductViewModel
import com.brandsin.store.utils.Utils
import com.brandsin.store.model.constants.Params
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RejectRefundableFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRejectRefundableBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RefundableProductViewModel by activityViewModels()

    private lateinit var reason: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRejectRefundableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBtnListener()
        subscribeData()
    }

    private fun setBtnListener() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            if (validate()){
                viewModel.updateStatusRefundableProduct(
                    status = "rejected",
                    note = reason
                )
            }
        }
    }

    private fun subscribeData() {
        viewModel.updateStatusRefundableProductResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    if (it.data?.success == true) {
                        showToast(it.data.message.toString(), 2)
                        dismiss()
                    } else {
                        showToast(it.data?.message.toString(), 1)
                    }
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 2)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                    viewModel.obsIsEmpty.set(false)
                    viewModel.obsIsFull.set(false)
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(false)
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)
                }

                else -> {}
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        fun showErrorAndToast(editText: EditText, errorMessage: String) {
            editText.error = errorMessage
            showToast(errorMessage, 1)
            isValid = false
        }

        reason = binding.edtReason.text.toString().trim()

        when {
            reason.isEmpty() -> showErrorAndToast(
                binding.edtReason,
                getString(R.string.please_enter_reason_for_rejection)
            )
        }

        return isValid
    }

    fun showToast(msg: String, type: Int) {
        // success 2
        // false  1
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}