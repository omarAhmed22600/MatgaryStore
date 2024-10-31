package com.brandsin.store.ui.main.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentMyProductsBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.delete.DeleteProductResponse
import com.brandsin.store.model.main.products.list.ProductCategoriesData
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.dialogs.confirm.DialogConfirmFragment
import com.brandsin.store.ui.main.updateproduct.UpdateProductActivity
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import timber.log.Timber

class MyProductsFragment : BaseHomeFragment(), Observer<Any?>
{
    lateinit var binding: HomeFragmentMyProductsBinding
    lateinit var viewModel: MyProductsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = HomeFragmentMyProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MyProductsViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.my_products))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.productsAdapter.editLiveData.observe(viewLifecycleOwner) {
            /*val intent = Intent(requireActivity(), UpdateProductActivity::class.java)
            intent.putExtra(Params.PRODUCT_ITEM , it)
            startActivityForResult(intent, Codes.UPDATE_PRODUCT)*/
            val bundle = Bundle().apply {
                putSerializable("productItem", it)
            }
            findNavController().navigate(R.id.nav_add_product,bundle)
        }

        viewModel.productsAdapter.deleteLiveData.observe(viewLifecycleOwner, {
            val bundle = Bundle()
            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, MyApp.getInstance().getString(R.string.delete_product_warning))
            bundle.putString(Params.DIALOG_CONFIRM_POSITIVE, MyApp.getInstance().getString(R.string.confirm))
            bundle.putString(Params.DIALOG_CONFIRM_NEGATIVE, MyApp.getInstance().getString(R.string.ignore))
            bundle.putSerializable(Params.PRODUCT_ITEM, it)
            Utils.startDialogActivity(requireActivity(), DialogConfirmFragment::class.java.name, Codes.DIALOG_CONFIRM_REQUEST, bundle)
        })

        observe(viewModel.categoryAdapter.categoryLiveData) {
            when(it) {
                is ProductCategoriesData -> {
                    viewModel.filterProducts(it.id)
                    viewModel.categoryId=it.id
                }
            }
        }

        observe(viewModel.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    showToast(it.message.toString(), 1)
                }
                Status.SUCCESS_MESSAGE -> {
                    showToast(it.message.toString(), 2)
                }
                Status.SUCCESS -> {
                    when (it.data) {
                        is DeleteProductResponse -> {
                            viewModel.getStoreProducts()
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getUserStatus()
        }

    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        it.let {
            when (it) {
//                Codes.LOGIN_CLICKED -> {
//                    PrefMethods.saveIsAskedToLogin(true)
//                    requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
//                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Codes.DIALOG_CONFIRM_REQUEST && data != null)
        {
            if (data.hasExtra(Params.DIALOG_CLICK_ACTION))
            {
                when {
                    data.getIntExtra(Params.DIALOG_CLICK_ACTION,1) == 1 -> {
                        if (data.getIntExtra(Params.PRODUCT_ITEM,0) != 0) {
                            viewModel.deleteProductRequest.product_id = data.getIntExtra(Params.PRODUCT_ITEM,0)
                            viewModel.deleteProduct()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserStatus()
    }
}