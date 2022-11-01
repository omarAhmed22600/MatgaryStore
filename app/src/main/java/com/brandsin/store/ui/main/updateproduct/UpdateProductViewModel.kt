package com.brandsin.store.ui.main.updateproduct

import com.google.gson.Gson
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.DataItem
import com.brandsin.store.model.main.products.ListUnitResponse
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesData
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesResponse
import com.brandsin.store.model.main.products.update.SkuUpdateProductItem
import com.brandsin.store.model.main.products.update.UpdateProductRequest
import com.brandsin.store.model.main.products.update.UpdateProductResponse
import com.brandsin.store.model.profile.updatestore.UploadRequest
import com.brandsin.store.model.profile.updatestore.UploadResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UpdateProductViewModel : BaseViewModel() {

    //Update
    var categoriesList: ArrayList<ProductCategoriesData> = ArrayList()
    var unitList: ArrayList<DataItem> = ArrayList()
    var updateProductRequest = UpdateProductRequest()
    var updateProductSkuAdapter = UpdateProductSkuAdapter()
    var validate = false
    var skuPosition = -1

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
                updateProductSkuAdapter.setData(updateProductRequest.skusList as ArrayList<SkuUpdateProductItem>,unitList)
            }
        }
    }

    fun onAddSizeClicked() {
        updateProductSkuAdapter.updateList("","",skuPosition)
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


    fun onUpdateClicked() {
        updateProductSkuAdapter.getData()
    }

    fun validation(){
        if (updateProductRequest.categoriesIds == null || updateProductRequest.categoriesIds!!.size == 0) {
            setValue(Codes.EMPTY_TYPE)
        }else if ( updateProductRequest.name  == null  || updateProductRequest.name.toString().trim().isEmpty() ) {
            setValue(Codes.NAME_EMPTY)
//        }else if ( updateProductRequest.nameEn  == null  || updateProductRequest.nameEn.toString().trim().isEmpty() ) {
//            setValue(Codes.EMPTY_PRODUCT_NAME_EN)
        }else if (  updateProductRequest.description == null  || updateProductRequest.description.toString().trim().isEmpty() ) {
            setValue(Codes.EMPTY_DESCRIPTION)
//        }else if (  updateProductRequest.descriptionEn == null  || updateProductRequest.descriptionEn.toString().trim().isEmpty() ) {
//            setValue(Codes.EMPTY_PRODUCT_DESCRIPTION_EN)
        }else if (   updateProductRequest.mediaId == null) {
            setValue(Codes.EMPTY_IMAGE)
        }else if (  updateProductRequest.skusList == null || updateProductRequest.skusList!!.size == 0 ){
            setValue(Codes.EMPTY_SKU)
        }else if (updateProductRequest.skusList!!.size > 0  && !validate) {
            for (item in updateProductRequest.skusList!!) {
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
            updateProduct()
        }
    }
    fun updateProduct() {

        var gson = Gson()
        updateProductRequest.skus  = gson.toJson(updateProductRequest.skusList)

        if (updateProductRequest.skusList!!.size>1){
            updateProductRequest.type = "variable"
        }else{
            updateProductRequest.type = "simple"
        }

        if ( updateProductRequest.nameEn  == null  || updateProductRequest.nameEn.toString().trim().isEmpty() ) {
            updateProductRequest.nameEn = ""
        }
        if ( updateProductRequest.descriptionEn == null  || updateProductRequest.descriptionEn.toString().trim().isEmpty() ) {
            updateProductRequest.descriptionEn = ""
        }
        updateProductRequest.storeId = PrefMethods.getStoreData()!!.id!!.toInt()
        updateProductRequest.locale = PrefMethods.getLanguage()


        obsIsVisible.set(true)
        requestCall<UpdateProductResponse?>({
            withContext(Dispatchers.IO) { return@withContext getApiRepo().updateProduct(updateProductRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.isSuccess) {
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
                    updateProductRequest.mediaId!!.add(i,res.data!!.id!!)
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