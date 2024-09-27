package com.brandsin.store.ui.main.addproduct

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.DataItem
import com.brandsin.store.model.main.products.ListUnitResponse
import com.brandsin.store.model.main.products.add.AddProductRequest
import com.brandsin.store.model.main.products.add.AddProductResponse

import com.brandsin.store.model.main.products.productcategories.ProductCategoriesData
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesResponse
import com.brandsin.store.model.profile.updatestore.UploadRequest
import com.brandsin.store.model.profile.updatestore.UploadResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AddProductViewModel : BaseViewModel()
{

    //add
    var categoriesList: ArrayList<ProductCategoriesData> = ArrayList()
    var unitList: ArrayList<DataItem> = ArrayList()
    var addProductRequest = AddProductRequest()
    var addproductSkuAdapter = AddProductSkuAdapter()
    var validate = false
    var skuPosition = -1
    var imageList = MutableLiveData(mutableListOf<PhotoModel>())
    var fileImageList = MutableLiveData(mutableListOf<File>())
    var uploadRequest = UploadRequest ()

    init {
        getProductCategories()
    }

    private fun getProductCategories() {
        requestCall<ProductCategoriesResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                    .getProductCategories(0, PrefMethods.getLanguage(), PrefMethods.getStoreData()!!.id!!.toInt())
            }
        })
        { res ->
            if (res!!.data!!.isNotEmpty()) {
                categoriesList = res.data as ArrayList<ProductCategoriesData>
            }
        }
    }

     fun getUnitsList(categoriesIds: ArrayList<Int>) {
        requestCall<ListUnitResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                        .getUnitsList(categoriesIds, PrefMethods.getLanguage())
            }
        })
        { res ->
            if (res!!.data!!.isNotEmpty()) {
                unitList = res.data as ArrayList<DataItem>
            }
        }
    }

    fun onAddSizeClicked() {
        addproductSkuAdapter.updateList("","",skuPosition)
    }

    fun onPhoto1Clicked() {
        setValue(Codes.SELECT_PHOTO1)
    }
    fun onPhoto2Clicked() {
        setValue(Codes.SELECT_PHOTO2)
    }
    fun onPhoto3Clicked() {
        setValue(Codes.SELECT_PHOTO3)
    }

    fun onPhoto4Clicked() {
        setValue(Codes.SELECT_PHOTO4)
    }

    fun onAddClicked() {
        addproductSkuAdapter.getData()
    }

    fun validation(){
        if (addProductRequest.categoriesIds == null || addProductRequest.categoriesIds!!.size == 0) {
            setValue(Codes.EMPTY_TYPE)
        }else if ( addProductRequest.name  == null  || addProductRequest.name.toString().trim().isEmpty() ) {
            setValue(Codes.NAME_EMPTY)
//        }else if ( addProductRequest.nameEn  == null  || addProductRequest.nameEn.toString().trim().isEmpty() ) {
//            setValue(Codes.EMPTY_PRODUCT_NAME_EN)
        }else if (  addProductRequest.description == null  || addProductRequest.description.toString().trim().isEmpty() ) {
            setValue(Codes.EMPTY_DESCRIPTION)
//        }else if (  addProductRequest.descriptionEn == null  || addProductRequest.descriptionEn.toString().trim().isEmpty() ) {
//            setValue(Codes.EMPTY_PRODUCT_DESCRIPTION_EN)
        }else if (   addProductRequest.mediaId == null) {
            setValue(Codes.EMPTY_IMAGE)
        }else if (  addProductRequest.skusList == null || addProductRequest.skusList!!.size == 0 ){
            setValue(Codes.EMPTY_SKU)
        }else if (addProductRequest.skusList!!.size > 0  && !validate) {
            for (item in addProductRequest.skusList!!) {
                if (item.name == null || item.name.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_SizeName)
                    validate =false
                    break
                } else if (item.inventory_value == null || item.inventory_value.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_InventoryValue)
                    validate =false
                    break
                } else if (item.regular_price == null || item.regular_price.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_Price)
                    validate =false
                    break
                } else if (item.code == null || item.code.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_CODE)
                    validate =false
                    break
                }else{
                    validate =true
                }
            }
        }else {
            createProduct()
        }
    }
    fun createProduct() {

        var gson = Gson()
        addProductRequest.skus  = gson.toJson(addProductRequest.skusList)

        if (addProductRequest.skusList!!.size>1){
            addProductRequest.type = "variable"
        }else{
            addProductRequest.type = "simple"
        }

        if ( addProductRequest.nameEn  == null  || addProductRequest.nameEn.toString().trim().isEmpty() ) {
            addProductRequest.nameEn = ""
        }
        if ( addProductRequest.descriptionEn == null  || addProductRequest.descriptionEn.toString().trim().isEmpty() ) {
            addProductRequest.descriptionEn = ""
        }

        addProductRequest.storeId = PrefMethods.getStoreData()!!.id!!.toInt()
        addProductRequest.locale = PrefMethods.getLanguage()

        obsIsVisible.set(true)
        requestCall<AddProductResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().createProduct(addProductRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }

    fun upload(i: Int){
        obsIsVisible.set(true)
        requestCall<UploadResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().upload(uploadRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    addProductRequest.mediaId!!.add(i,res.data!!.id!!)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message)
                }
            }
        }
    }
    fun onCategoriesClicked() {
        if (categoriesList.isNotEmpty()) {
            setValue(Codes.PRODUCT_CATEGORIES_CLICKED)
        }else{
            getProductCategories()
        }
    }
}