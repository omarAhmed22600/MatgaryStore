package com.brandsin.store.ui.main.refundableProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentRefundableProductDetailsBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.refundableProduct.model.RefundableDetails
import com.brandsin.store.ui.main.refundableProduct.viewmodel.RefundableProductViewModel
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RefundableProductDetailsFragment : BaseHomeFragment() {

    private var _binding: FragmentRefundableProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RefundableProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRefundableProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.return_requests))

        viewModel.getRefundableDetailsByRefundableId()

        setBtnListener()
        subscribeData()
    }

    private fun initViews(refundableDetails: RefundableDetails?) {

        if (refundableDetails?.status?.contains("approval") == true ||
            refundableDetails?.status?.contains("rejected") == true
        ) {
            binding.btnAccept.gone()
            binding.btnReject.gone()
        } else {
            binding.btnAccept.visible()
            binding.btnReject.visible()
        }

        binding.productName.text = refundableDetails?.orderItem?.order?.user?.name.toString()

        binding.orderNumber.text = refundableDetails?.orderItem?.order?.orderNumber.toString()

        binding.orderDate.text = formatDate(refundableDetails?.createdAt ?: "")

        Glide.with(requireContext())
            .load(refundableDetails?.orderItem?.order?.user?.picture)
            .error(R.drawable.app_logo)
            .into(binding.imgProduct)

        Glide.with(requireContext())
            .load(refundableDetails?.image)
            .error(R.drawable.app_logo)
            .into(binding.imgRefundableProduct)

        binding.refundableReason.text = refundableDetails?.reason ?: ""

        binding.notes.text = refundableDetails?.note ?: ""

        binding.imgRefundableProduct.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("IMAGE", refundableDetails?.image)
            findNavController().navigate(R.id.imagePreviewFragment, bundle)
        }
    }

    private fun setBtnListener() {
        binding.btnAccept.setOnClickListener {
            viewModel.updateStatusRefundableProduct(
                status = "approval",
                note = null
            )
        }

        binding.btnReject.setOnClickListener {
            // show bottom sheet to enter reason
            val bottomSheetFragment = RejectRefundableFragment()
            bottomSheetFragment.show(childFragmentManager, "RejectRefundableFragment")
        }
    }

    private fun subscribeData() {
        viewModel.getRefundableDetailsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initViews(it.data?.refundableDetails)
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

        viewModel.updateStatusRefundableProductResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    if (it.data?.success == true) {
                        showToast(it.data.message.toString(), 2)
                        findNavController().navigateUp()
                    } else {
                        showToast(it.data?.message.toString(), 1)
                    }
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 1)
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

    private fun formatDate(data: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy  HH:mm", Locale.getDefault())

        // Format the parsed date to the desired output format
        return outputFormat.format(inputFormat.parse(data) as Date)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}