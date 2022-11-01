package com.brandsin.store.ui.main.updateproduct

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityUpdateProductBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.list.ProductsItem
import com.brandsin.store.model.main.products.update.SkuUpdateProductItem
import com.brandsin.store.model.main.products.update.UpdateProductResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.addproduct.DialogAddProductFragment
import com.brandsin.store.ui.dialogs.productcategories.DialogProductCategoriesFragment
import com.brandsin.store.ui.dialogs.productunit.DialogProductUnitFragment
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.user.model.constants.Params
import com.brandsin.user.utils.map.PermissionUtil
import timber.log.Timber
import java.io.File
import java.util.ArrayList

class UpdateProductActivity : AppCompatActivity(), Observer<Any?>
{
    lateinit var binding : ActivityUpdateProductBinding
    lateinit var viewModel : UpdateProductViewModel
    var productData = ProductsItem()
    var categoriesNames: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_product)
        viewModel = ViewModelProvider(this).get(UpdateProductViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

        binding.ibBack.setOnClickListener {
            finish()
        }

        when {
            intent.hasExtra(Params.PRODUCT_ITEM) -> {
                when {
                    intent.getSerializableExtra(Params.PRODUCT_ITEM) != null -> {
                        productData = intent.extras!!.getSerializable(Params.PRODUCT_ITEM) as ProductsItem

                        viewModel.updateProductRequest.id = productData.id
                        viewModel.updateProductRequest.name = productData.name
                        viewModel.updateProductRequest.description = productData.description
                        viewModel.updateProductRequest.nameEn = productData.nameEn
                        viewModel.updateProductRequest.descriptionEn = productData.descriptionEn
                        viewModel.updateProductRequest.type = productData.type
                        viewModel.updateProductRequest.storeId = productData.storeId
                        productData.categories!!.forEach { viewModel.updateProductRequest.categoriesIds!!.add(it!!.id!!.toInt()) }
                        productData.categories!!.forEach { categoriesNames.add(it!!.name.toString()) }
                        binding.productCategory.text =  categoriesNames.joinToString { it -> "$it" }
                        viewModel.updateProductRequest.skusList = productData.skus as ArrayList<SkuUpdateProductItem>
                        viewModel.getUnitsList(viewModel.updateProductRequest.categoriesIds!!)
                        viewModel.updateProductSkuAdapter.setData(  viewModel.updateProductRequest.skusList as ArrayList<SkuUpdateProductItem>,  viewModel.unitList)

                        if (productData.imagesIds?.size!! >= 1) {
                            binding.notPhoto1.visibility = View.GONE
                            binding.ivImg1.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![0]?.url).into(binding.ivPhoto1)
                            if (viewModel.updateProductRequest.mediaId == null){
                                viewModel.updateProductRequest.mediaId = ArrayList ()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![0]?.id!!)
                        }
                        if (productData.imagesIds!!.size >= 2) {
                            binding.notPhoto2.visibility = View.GONE
                            binding.ivImg2.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![1]?.url).into(binding.ivPhoto2)
                            if (viewModel.updateProductRequest.mediaId == null){
                                viewModel.updateProductRequest.mediaId = ArrayList ()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![1]?.id!!)
                        }

                        if (productData.imagesIds!!.size >=3) {
                            binding.notPhoto3.visibility = View.GONE
                            binding.ivImg3.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![2]?.url).into(binding.ivPhoto3)
                            if (viewModel.updateProductRequest.mediaId == null){
                                viewModel.updateProductRequest.mediaId = ArrayList ()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![2]?.id!!)
                        }

                        if (productData.imagesIds!!.size >=4) {
                            binding.notPhoto4.visibility = View.GONE
                            binding.ivImg4.visibility = View.VISIBLE
                            Glide.with(this).load(productData.imagesIds!![3]?.url).into(binding.ivPhoto4)
                            if (viewModel.updateProductRequest.mediaId == null){
                                viewModel.updateProductRequest.mediaId = ArrayList ()
                            }
                            viewModel.updateProductRequest.mediaId!!.add(productData.imagesIds!![3]?.id!!)
                        }
                    }
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
                        is UpdateProductResponse -> {
                            val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.UPDATE_PRODUCT)
                            Utils.startDialogActivity(this, DialogAddProductFragment::class.java.name, Codes.DIALOG_PRODUCT_REQUEST, bundle)
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }

        viewModel.updateProductSkuAdapter.productSkuLiveData.observe(this, {
            if (it != null){
                viewModel.updateProductRequest.skusList = it
                viewModel.validation()
            }
        })

        viewModel.updateProductSkuAdapter.productUnitLiveData.observe(this, {
            if (viewModel.updateProductRequest.categoriesIds != null) {
                if (it != null) {
                    viewModel.skuPosition = it

                    val bundle = Bundle()
                    bundle.putSerializable(Params.PRODUCT_UNIT, viewModel.unitList)
                    Utils.startDialogActivity(
                        this,
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
                        if (viewModel.updateProductRequest.categoriesIds !=null){
                            bundle.putSerializable(Params.PRODUCT_CATEGORIES_DATA_EDIT, viewModel.updateProductRequest)
                        }
                        Utils.startDialogActivity(this, DialogProductCategoriesFragment::class.java.name, Codes.DIALOG_PRODUCT_CATEGORY_CODE, bundle)
                    }
                    Codes.EMPTY_TYPE -> {
                        showToast(getString(R.string.enter_type), 1)
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
                                if (viewModel.updateProductRequest.mediaId != null ){
                                    if (viewModel.updateProductRequest.deleteMediaId == null){
                                        viewModel.updateProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.updateProductRequest.mediaId!!.size >=3) {
                                        viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![2])
                                        viewModel.updateProductRequest.mediaId!!.removeAt(2)
                                    }
                                }else{
                                    viewModel.updateProductRequest.mediaId = ArrayList ()
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
                                if (viewModel.updateProductRequest.mediaId != null ){
                                    if (viewModel.updateProductRequest.deleteMediaId == null){
                                        viewModel.updateProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.updateProductRequest.mediaId!!.size >=4) {
                                        viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![3])
                                        viewModel.updateProductRequest.mediaId!!.removeAt(3)
                                    }
                                }else{
                                    viewModel.updateProductRequest.mediaId = ArrayList ()
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
                                if (viewModel.updateProductRequest.mediaId != null ){
                                    if (viewModel.updateProductRequest.deleteMediaId == null){
                                        viewModel.updateProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.updateProductRequest.mediaId!!.size >=5) {
                                        viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![4])
                                        viewModel.updateProductRequest.mediaId!!.removeAt(4)
                                    }
                                }else{
                                    viewModel.updateProductRequest.mediaId = ArrayList ()
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
                                if (viewModel.updateProductRequest.mediaId != null){
                                    if (viewModel.updateProductRequest.deleteMediaId == null){
                                        viewModel.updateProductRequest.deleteMediaId = ArrayList ()
                                    }
                                    if (viewModel.updateProductRequest.mediaId!!.size >=6) {
                                        viewModel.updateProductRequest.deleteMediaId!!.add(viewModel.updateProductRequest.mediaId!![5])
                                        viewModel.updateProductRequest.mediaId!!.removeAt(5)
                                    }
                                }else{
                                    viewModel.updateProductRequest.mediaId = ArrayList ()
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
                            viewModel.updateProductRequest.categoriesIds =  data.getIntegerArrayListExtra("productCategoryId")
                            viewModel.getUnitsList(viewModel.updateProductRequest.categoriesIds!!)
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

                            viewModel.updateProductSkuAdapter.updateList(productUnitId,productUnitName,viewModel.skuPosition)
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
                                val intent = Intent()
                                intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                                setResult(Codes.UPDATE_PRODUCT, intent)
                                finish()
                            }
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 2 -> {
                                startActivity(Intent(this, HomeActivity::class.java))
                                finishAffinity()
                            }
                        }
                    }
                }
            }
        }
    }

    fun pickImage(requestCode: Int) {
        val options = Options.init()
                .setRequestCode(requestCode) //Request code for activity results
                .setFrontfacing(false) //Front Facing camera on start
                .setExcludeVideos(true) //Option to exclude videos
        if (PermissionUtil.hasImagePermission(this)) {
            Pix.start(this, options)
        } else {
            observe(PermissionUtil.requestPermission(
                    this,
                    PermissionUtil.getImagePermissions()
            )
            ) {
                when (it) {
                    PermissionUtil.AppPermissionResult.AllGood -> Pix.start(
                            this,
                            options
                    )
                }
            }
        }
    }
    private fun showToast(msg : String, type : Int)
    {
        val bundle = Bundle()
        bundle.putString(Params.DIALOG_TOAST_MESSAGE, msg)
        bundle.putInt(Params.DIALOG_TOAST_TYPE, type)
        Utils.startDialogActivity(this, DialogToastFragment::class.java.name, Codes.DIALOG_TOAST_REQUEST, bundle)
    }
}