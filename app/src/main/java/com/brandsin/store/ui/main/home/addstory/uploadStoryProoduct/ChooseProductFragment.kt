package com.brandsin.store.ui.main.home.addstory.uploadStoryProoduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentChooseProductBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.ui.main.home.addstory.AddStoryViewModel
import com.brandsin.store.ui.main.home.addstory.uploadStoryProoduct.adapter.ChooseProductAdapter
import com.brandsin.store.utils.Utils
import com.brandsin.store.model.constants.Params
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseProductFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentChooseProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddStoryViewModel by activityViewModels()

    private lateinit var chooseProductAdapter: ChooseProductAdapter

    // private var productItem: ProductsItem? = null

    private val btnItemClickCallBack: (productsItem: ProductsItem) -> Unit =
        { product ->
            // val newList = viewModel.changeSelectedProduct(productsItem, position)
            // productItem = productsItem?.get(position)
            // chooseProductAdapter.submitData(newList)

            viewModel.request.productId = product.id
            viewModel.productName = product.name.toString()
            viewModel.productSalePrice = product.skus?.get(0)?.sale_price.toString()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChooseProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStoreProductsList()

        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun initRecycler() {
        binding.rvChooseProduct.apply {
            chooseProductAdapter = ChooseProductAdapter(btnItemClickCallBack)
            chooseProductAdapter.setHasStableIds(true)
            adapter = chooseProductAdapter
        }
    }

    private fun setBtnListener() {
        // To perform a search/filter operation:
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                chooseProductAdapter.filter.filter(newText)
                chooseProductAdapter.notifyDataSetChanged()
                return true
            }
        })

        binding.btnCancel.setOnClickListener {
            viewModel.request.productId = null
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            if (viewModel.request.productId != null) {
                findNavController().navigate(R.id.uploadStoryProductFragment)
            } else {
                showToast(getString(R.string.please_choose_product), 1)
            }
        }
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

    private fun subscribeData() {
        viewModel.getStoreProductsListResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    chooseProductAdapter.submitData(it.data?.data?.products)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.request.productId = null
        _binding = null
    }
}