package com.brandsin.store.ui.main.discountCoupon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentDiscountCouponsBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.discountCoupon.adapter.DiscountCouponsAdapter
import com.brandsin.store.ui.main.discountCoupon.model.CouponItem
import com.brandsin.store.ui.main.discountCoupon.viewmodel.DiscountCouponViewModel
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible

class DiscountCouponsFragment : BaseHomeFragment() {

    private var _binding: FragmentDiscountCouponsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscountCouponViewModel by activityViewModels()

    private lateinit var discountCouponsAdapter: DiscountCouponsAdapter

    private val btnEditClickCallBack: (couponItem: CouponItem) -> Unit = { couponItem ->
        viewModel.typeClicked = "EditCoupon"
        viewModel.couponItem = couponItem
        findNavController().navigate(R.id.addAndUpdateDiscountCouponFragment)
    }

    private val btnDeleteClickCallBack: (couponItem: CouponItem) -> Unit = { couponItem ->
        viewModel.deleteCouponById(couponItem.id!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDiscountCouponsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.discount_coupons))

        viewModel.getAllCoupons()

        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun initRecycler() {
        binding.rvDiscountCoupons.apply {
            discountCouponsAdapter = DiscountCouponsAdapter(
                btnEditClickCallBack,
                btnDeleteClickCallBack
            )
            adapter = discountCouponsAdapter
        }
    }

    private fun setBtnListener() {
        binding.btnCreateNewCoupon.setOnClickListener {
            viewModel.typeClicked = "AddNewCoupon"
            findNavController().navigate(R.id.addAndUpdateDiscountCouponFragment)
        }
    }

    private fun subscribeData() {
        viewModel.getAllCouponsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    if (it.data?.couponItem?.isEmpty() == true || it.data?.couponItem?.size == 0) {
                        binding.emptyLayout.visible()
                        binding.rvDiscountCoupons.gone()
                    } else {
                        binding.emptyLayout.gone()
                        binding.rvDiscountCoupons.visible()

                        // set data in adapter
                        discountCouponsAdapter.submitData(it.data?.couponItem)
                    }
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 1)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                    viewModel.obsIsFull.set(false)
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(false)
                    viewModel.obsIsFull.set(false)
                }

                else -> {}
            }
        }

        viewModel.deleteCouponByIdResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    showToast(it.data?.message ?: "", 2)
                    viewModel.getAllCoupons()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}