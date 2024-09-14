package com.brandsin.store.ui.main.refundableProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentRefundableProductsBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.refundableProduct.adapter.RefundableProductAdapter
import com.brandsin.store.ui.main.refundableProduct.model.RefundableProductItem
import com.brandsin.store.ui.main.refundableProduct.viewmodel.RefundableProductViewModel
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible

class RefundableProductsFragment : BaseHomeFragment() {

    private var _binding: FragmentRefundableProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RefundableProductViewModel by activityViewModels()

    private lateinit var refundableProductAdapter: RefundableProductAdapter

    private val btnDetailsClickCallBack: (refundableProductItem: RefundableProductItem) -> Unit =
        { refundableProductItem ->
            viewModel.refundableId = refundableProductItem.refundableId
            findNavController().navigate(R.id.refundableProductDetailsFragment)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRefundableProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.return_requests))

        viewModel.getAllRefundableProduct()

        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun setBtnListener() {
        binding.root.setOnRefreshListener {
            viewModel.getAllRefundableProduct()
            binding.root.isRefreshing = false
        }
    }

    private fun initRecycler() {
        binding.rvRefundableProducts.apply {
            refundableProductAdapter = RefundableProductAdapter(btnDetailsClickCallBack)
            adapter = refundableProductAdapter
        }
    }

    private fun subscribeData() {
        viewModel.getAllRefundableProductResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    if (it.data?.refundableProductList?.isEmpty() == true) {
                        binding.emptyLayout.visible()
                        binding.rvRefundableProducts.gone()
                    } else {
                        binding.emptyLayout.gone()
                        binding.rvRefundableProducts.visible()
                        refundableProductAdapter.submitData(it.data?.refundableProductList)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}