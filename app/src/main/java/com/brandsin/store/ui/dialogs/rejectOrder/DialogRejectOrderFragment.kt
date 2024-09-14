package com.brandsin.store.ui.dialogs.rejectOrder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentDialogRejectOrderBinding
import com.brandsin.store.ui.dialogs.rejectOrder.adapter.RejectedReasonAdapter
import com.brandsin.store.ui.dialogs.rejectOrder.model.RejectReasonModel

class DialogRejectOrderFragment(
    private val btnRejectedOrderCallback: () -> Unit, // rejectReason: RejectReasonModel
) : DialogFragment() {

    private var _binding: FragmentDialogRejectOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var rejectedReasonAdapter: RejectedReasonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDialogRejectOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the window background to transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set the window attributes for match_parent
        val params = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params

        binding.rvRejectedReason.apply {
            rejectedReasonAdapter = RejectedReasonAdapter()
            rejectedReasonAdapter.submitData(getRejectedReasonsList())
            adapter = rejectedReasonAdapter
        }

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.btnCancel.setOnClickListener {
            dismissNow()
        }

        binding.btnRejectOrder.setOnClickListener {
            dismissNow()
            btnRejectedOrderCallback.invoke()
        }
    }

    private fun getRejectedReasonsList(): ArrayList<RejectReasonModel> {
        val rejectedReasonsList: ArrayList<RejectReasonModel> = ArrayList()

        rejectedReasonsList.add(RejectReasonModel(1, getString(R.string.store_is_busy)))
        rejectedReasonsList.add(RejectReasonModel(2, getString(R.string.i_don_t_have_delivery)))
        rejectedReasonsList.add(RejectReasonModel(3, getString(R.string.product_not_available)))
        rejectedReasonsList.add(RejectReasonModel(4, getString(R.string.bad_weather)))
        rejectedReasonsList.add(RejectReasonModel(5, getString(R.string.store_is_closed)))

        return rejectedReasonsList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}