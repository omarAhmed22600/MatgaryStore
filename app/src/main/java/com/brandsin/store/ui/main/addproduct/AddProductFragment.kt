package com.brandsin.store.ui.main.addproduct

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fxn.pix.Pix
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentAddProductBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.add.AddProductResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.addproduct.DialogAddProductFragment
import com.brandsin.store.ui.dialogs.productcategories.DialogProductCategoriesFragment
import com.brandsin.store.ui.dialogs.productunit.DialogProductUnitFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.user.model.constants.Params
import timber.log.Timber
import java.io.File

class AddProductFragment : BaseHomeFragment(), Observer<Any?>
{
    lateinit var binding : HomeFragmentAddProductBinding
    lateinit var viewModel : AddProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HomeFragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.add_product))

        viewModel.mutableLiveData.observe(this, this)

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
                        is AddProductResponse -> {
                            val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.ADD_PRODUCT)
                            Utils.startDialogActivity(requireActivity(), DialogAddProductFragment::class.java.name, Codes.DIALOG_PRODUCT_REQUEST, bundle
                            )
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }

        viewModel.addproductSkuAdapter.productSkuLiveData.observe(viewLifecycleOwner, {
            if (it != null){
                viewModel.addProductRequest.skusList = it
                viewModel.validation()
            }
        })

        viewModel.addproductSkuAdapter.productUnitLiveData.observe(viewLifecycleOwner, {
            if (viewModel.addProductRequest.categoriesIds != null) {
                if (it != null) {
                    viewModel.skuPosition = it

                    val bundle = Bundle()
                    bundle.putSerializable(Params.PRODUCT_UNIT, viewModel.unitList)
                    Utils.startDialogActivity(
                        requireActivity(),
                        DialogProductUnitFragment::class.java.name,
                        Codes.DIALOG_PRODUCT_UNIT_CODE,
                        bundle
                    )
                }
            }else{
                showToast(getString(R.string.enter_product_type), 1)
            }
        })
    }

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> it.let {
                when (it) {
                    Codes.SELECT_PHOTO1 -> {
                        pickImage(Codes.SELECT_PHOTO1)
                    }
                    Codes.SELECT_PHOTO2 -> {
                        pickImage(Codes.SELECT_PHOTO2)
                    }
                    Codes.SELECT_PHOTO3 -> {
                        pickImage(Codes.SELECT_PHOTO3)
                    }
                    Codes.SELECT_PHOTO4 -> {
                        pickImage(Codes.SELECT_PHOTO4)
                    }
                    Codes.PRODUCT_CATEGORIES_CLICKED -> {
                        val bundle = Bundle()
                        bundle.putSerializable(Params.PRODUCT_CATEGORIES, viewModel.categoriesList)
                        if (viewModel.addProductRequest.categoriesIds !=null){
                            bundle.putSerializable(Params.PRODUCT_CATEGORIES_DATA, viewModel.addProductRequest)
                        }
                        Utils.startDialogActivity(requireActivity(), DialogProductCategoriesFragment::class.java.name, Codes.DIALOG_PRODUCT_CATEGORY_CODE, bundle)
                    }
                    Codes.EMPTY_TYPE -> {
                        showToast(getString(R.string.enter_product_type), 1)
                    }
                    Codes.NAME_EMPTY -> {
                        showToast(getString(R.string.enter_product_name_ar), 1)
                    }
                    Codes.EMPTY_DESCRIPTION -> {
                        showToast(getString(R.string.enter_description_ar), 1)
                    }
                    Codes.EMPTY_PRODUCT_NAME_EN -> {
                        showToast(getString(R.string.enter_product_name_en), 1)
                    }
                    Codes.EMPTY_PRODUCT_DESCRIPTION_EN -> {
                        showToast(getString(R.string.enter_description_en), 1)
                    }
                    Codes.EMPTY_IMAGE -> {
                        showToast(getString(R.string.enter_image), 1)
                    }
                    Codes.EMPTY_SKU -> {
                        showToast(getString(R.string.enter_sku_item), 1)
                    }
                    Codes.EMPTY_SizeName -> {
                        showToast(getString(R.string.enter_product_size_name), 1)
                    }
                    Codes.EMPTY_InventoryValue -> {
                        showToast(getString(R.string.enter_product_inventory_value), 1)
                    }
                    Codes.EMPTY_Price -> {
                        showToast(getString(R.string.enter_product_price), 1)
                    }
                    Codes.EMPTY_CODE -> {
                        showToast(getString(R.string.enter_product_code), 1)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Codes.SELECT_PHOTO1 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto1.visibility = View.GONE
                                binding.ivImg1.visibility = View.VISIBLE
                                binding.ivPhoto1.setImageURI(array[0].toUri())
                                if (viewModel.addProductRequest.mediaId != null ){
                                    if (viewModel.addProductRequest.deleteMediaId == null){
                                        viewModel.addProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.addProductRequest.mediaId!!.size >=3) {
                                        viewModel.addProductRequest.deleteMediaId!!.add(viewModel.addProductRequest.mediaId!![2])
                                        viewModel.addProductRequest.mediaId!!.removeAt(2)
                                    }
                                }else{
                                    viewModel.addProductRequest.mediaId = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "marketplace-product-gallery"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(0)
                            } }
                    }
                    Codes.SELECT_PHOTO2 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto2.visibility = View.GONE
                                binding.ivImg2.visibility = View.VISIBLE
                                binding.ivPhoto2.setImageURI(array[0].toUri())
                                if (viewModel.addProductRequest.mediaId != null ){
                                    if (viewModel.addProductRequest.deleteMediaId == null){
                                        viewModel.addProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.addProductRequest.mediaId!!.size >=4) {
                                        viewModel.addProductRequest.deleteMediaId!!.add(viewModel.addProductRequest.mediaId!![3])
                                        viewModel.addProductRequest.mediaId!!.removeAt(3)
                                    }
                                }else{
                                    viewModel.addProductRequest.mediaId = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "marketplace-product-gallery"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(1)
                            } }
                    }
                    Codes.SELECT_PHOTO3 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto3.visibility = View.GONE
                                binding.ivImg3.visibility = View.VISIBLE
                                binding.ivPhoto3.setImageURI(array[0].toUri())
                                if (viewModel.addProductRequest.mediaId != null ){
                                    if (viewModel.addProductRequest.deleteMediaId == null){
                                        viewModel.addProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.addProductRequest.mediaId!!.size >=5) {
                                        viewModel.addProductRequest.deleteMediaId!!.add(viewModel.addProductRequest.mediaId!![4])
                                        viewModel.addProductRequest.mediaId!!.removeAt(4)
                                    }
                                }else{
                                    viewModel.addProductRequest.mediaId = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "marketplace-product-gallery"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(2)
                            } }
                    }
                    Codes.SELECT_PHOTO4 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto4.visibility = View.GONE
                                binding.ivImg4.visibility = View.VISIBLE
                                binding.ivPhoto4.setImageURI(array[0].toUri())
                                if (viewModel.addProductRequest.mediaId != null){
                                    if (viewModel.addProductRequest.deleteMediaId == null){
                                        viewModel.addProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.addProductRequest.mediaId!!.size >=6) {
                                        viewModel.addProductRequest.deleteMediaId!!.add(viewModel.addProductRequest.mediaId!![5])
                                        viewModel.addProductRequest.mediaId!!.removeAt(5)
                                    }
                                }else{
                                    viewModel.addProductRequest.mediaId = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "marketplace-product-gallery"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(3)
                            } }
                    }
                }
            }
        }

        when {
            requestCode == Codes.DIALOG_PRODUCT_CATEGORY_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            binding.productCategory.text = data.getStringExtra("productCategoryNames")
                            viewModel.addProductRequest.categoriesIds =  data.getIntegerArrayListExtra("productCategoryId")
                            viewModel.getUnitsList(viewModel.addProductRequest.categoriesIds!!)
                        }
                    }
                }
            }
        }

        when {
            requestCode == Codes.DIALOG_PRODUCT_UNIT_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            var productUnitId = data.getStringExtra("productUnitId").toString()
                            var productUnitName = data.getStringExtra("productUnitName").toString()

                            viewModel.addproductSkuAdapter.updateList(productUnitId,productUnitName,viewModel.skuPosition)
                        }
                    }
                }
            }
        }

        when {
            requestCode == Codes.DIALOG_PRODUCT_REQUEST && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                                findNavController().navigate(R.id.add_products_to_products)
                            }
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 2 -> {
                                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                                requireActivity().finishAffinity()
                            }
                        }
                    }
                }
            }
        }
    }
}