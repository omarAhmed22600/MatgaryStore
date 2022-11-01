package com.brandsin.store.ui.dialogs.productcategories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.databinding.DialogProductCategoriesBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.add.AddProductRequest
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesData
import com.brandsin.store.model.main.products.update.UpdateProductRequest
import com.brandsin.user.model.constants.Params

class DialogProductCategoriesFragment  : DialogFragment(), Observer<Any?>
{
    lateinit  var  binding: DialogProductCategoriesBinding
    lateinit var viewModel : ProductCategoriesViewModel

    var categoriesList: ArrayList<ProductCategoriesData> = ArrayList()
    var addProductRequest = AddProductRequest()
    var updateProductRequest = UpdateProductRequest()
    var productCategoryNames = ArrayList<String>()
    var productCategoryId = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            arguments != null -> {
                when {
                    requireArguments().containsKey(Params.PRODUCT_CATEGORIES) -> {
                        categoriesList = (requireArguments().getSerializable(Params.PRODUCT_CATEGORIES) as ArrayList<ProductCategoriesData>)
                        if (requireArguments().getSerializable(Params.PRODUCT_CATEGORIES_DATA)!=null){
                            addProductRequest= requireArguments().getSerializable(Params.PRODUCT_CATEGORIES_DATA) as AddProductRequest
                        }
                        if (requireArguments().getSerializable(Params.PRODUCT_CATEGORIES_DATA_EDIT)!=null){
                            updateProductRequest= requireArguments().getSerializable(Params.PRODUCT_CATEGORIES_DATA_EDIT) as UpdateProductRequest
                        }
                    }
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogProductCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProductCategoriesViewModel::class.java)
        binding.viewModel = viewModel

        if (categoriesList.isNotEmpty()) {
            viewModel.categoriesList = categoriesList
            viewModel.productCategoriesAdapter.updateList(
                    categoriesList,
                    addProductRequest,updateProductRequest
            )
        }

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.productCategoriesAdapter.orderAddonsLiveData.observe(this, Observer { o ->
            if (o is ProductCategoriesData) {
                val item: ProductCategoriesData = o
                if (!productCategoryId.contains(item.id)){
                    productCategoryId.add(item.id!!)
                    productCategoryNames.add(item.name!!)
                }else{
                    productCategoryId.remove(item.id)
                    productCategoryNames.remove(item.name)
                }
            }
        })
    }

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> when (it) {
                Codes.CONFIRM_CLICKED -> {
                    val intent = Intent()
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                    intent.putIntegerArrayListExtra("productCategoryId",productCategoryId)
                    intent.putExtra("productCategoryNames", productCategoryNames.joinToString { it -> "$it" })
                    requireActivity().setResult(Codes.DIALOG_PRODUCT_CATEGORY_CODE, intent)
                    requireActivity().finish()
                }
                Codes.CANCEL_CLICKED -> {
                    val intent = Intent()
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                    requireActivity().setResult(Codes.DIALOG_PRODUCT_CATEGORY_CODE, intent)
                    requireActivity().finish()
                }
            }
        }
    }
}