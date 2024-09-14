package com.brandsin.store.ui.main.categories.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentEditCategoryDialogBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.ui.main.categories.CategoriesViewModel
import com.brandsin.store.utils.Utils
import com.brandsin.store.model.constants.Params

class EditCategoryDialogFragment(
    private val successAddCategoryCallBack: () -> Unit,
) : DialogFragment() {

    private var _binding: FragmentEditCategoryDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoriesViewModel by viewModels()

    private lateinit var type: String
    private var categoryId: Int? = null
    private lateinit var nameAr: String
    private lateinit var nameEn: String

    companion object {
        // Factory method to create a new instance of the dialog with arguments
        fun newInstance(
            type: String,
            categoryId: Int?,
            nameAr: String?,
            nameEn: String?,
            successAddCategoryCallBack: () -> Unit,
        ): EditCategoryDialogFragment {
            val fragment = EditCategoryDialogFragment(successAddCategoryCallBack)
            val args = Bundle()
            args.putString("TYPE", type)
            args.putInt("CATEGORY_ID", categoryId ?: 0)
            args.putString("NAME_AR", nameAr)
            args.putString("NAME_EN", nameEn)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditCategoryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the background of the dialog window to be transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        type = requireArguments().getString("TYPE")!!
        if (type == "edit_category") {
            categoryId = requireArguments().getInt("CATEGORY_ID")
            nameAr = requireArguments().getString("NAME_AR")!!
            nameEn = requireArguments().getString("NAME_EN")!!

            binding.edtCategoryNameAr.setText(nameAr)
            binding.edtCategoryNameEn.setText(nameEn)
        }

        setBtnListener()
        subscribeData()
    }

    private fun setBtnListener() {
        binding.btnDone.setOnClickListener {
            if (validate()) {
                if (type == "edit_category") {
                    viewModel.editCategory(categoryId!!, nameAr, nameEn)
                } else {
                    viewModel.addCategory(nameAr, nameEn)
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun subscribeData() {
        viewModel.addAndEditCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    if (it.data?.success == true) {
                        successAddCategoryCallBack.invoke()
                        dismiss()
                    }
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)

                    // show error message
                    // showToast(it.message, 2)
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
        nameAr = binding.edtCategoryNameAr.text.toString().trim()
        nameEn = binding.edtCategoryNameEn.text.toString().trim()

        if (nameAr.isEmpty()) {
            binding.edtCategoryNameAr.error = getString(R.string.please_enter_category_name)
            showToast(getString(R.string.please_enter_category_name), 1)
            isValid = false
        } else {
            binding.edtCategoryNameAr.error = null
        }

        if (nameEn.isEmpty()) {
            binding.edtCategoryNameEn.error = getString(R.string.please_enter_category_name)
            showToast(getString(R.string.please_enter_category_name), 1)
            isValid = false
        } else {
            binding.edtCategoryNameEn.error = null
        }
        return isValid
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}